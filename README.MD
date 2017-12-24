# Cybersecurity Base - Project I

This is a fork of the base project for the first CSB project.  
Following is a writeup of all developer introduced bugs / [OWASP Top Ten](https://www.owasp.org/index.php/Top_10_2013-Top_10)  
  
---

This writeup as well as the referenced code can be found at https://github.com/Maddosaurus/cybersecuritybase-project    
It is a a better formatted (Markdown) Version of this report as well as the requeired codebase.


## **Issue**: Stored XSS in unescaped address input (A3)
There is an unescaped field that takes user input and is reflected onto the form page.  
If opened, the form page lists all previous entries in the signup process with name and address.  
If a user enters any valid HTML in the address field, it is reflected on every page load.

**Steps to reproduce:**
- Open the application (/form)
- Insert any name in the `user` filed, e.g. "John"
- Insert any valid HTML in the address field, e.g. `<script>alert(document.cookie)</script>`
- Press the button labeled `Submit`
- Observe a JavaScript popup showing the current session cookie

**How to fix:**
To achive described faulty behaviour, I had to introduce unsafe options in two places.  
If one wants to fix this, the member function `public void customize(ConfigurableEmbeddedServletContainer cesc)` in the class `CyberSecurityBaseProjectApplication` has to be removed completely, including the `implements` part in the class definition.  
Additionally, the span that reflects the address parameter in `form.html` has to be adjusted: currently, it is of type `th:utext`, it should be changed and therefore fixed to `th:text`:  
```
<span th:text="${signup.address}">user address</span>
```


## Exposure of the unconfigured database admin console (A5)
Exposing admin panel access with default credentials is extremely dangerous and bypasses all security measurements of the application.  
I exposed the h2 management console at /h2-console without any authentication.  

**Steps to reproduce:**
- Start the application
- Open the h2 console located at /h2-console
- Leave the default credentials untouched (User `sa`, without a password)
- Press the `Connect` button
- Observe the full admin console with complete access  

**How to fix:**
There is, as always, more than one way to fix this problem.  
One could set a password for the `sa` user, so that not everyone can login to the h2 interface.  
In my oppinion it would be an even better idea to remove access to this interface at all by removing the line ` http.authorizeRequests().antMatchers("/h2-console/*").permitAll();` completely from `SecurityConfiguration.java`.  


## **Issue**: No CSRF Tokens (A8)
No form in the application uses CSRF tokens.  
While there is no custom functionality implemented in this example, the absence of CSRF tokens is very dangerous.  

**Steps to reproduce**:
- Open the application (/form)
- Inspect the HTML of said form
- Observe missing CSRF fields and tokens

**How to fix**:  
To enforce the absence of CSRF tokens, `http.csrf().disable();`has been set in `SecurityConfiguration.java`, to be exact in the function `protected void configure(HttpSecurity http)`. To enable CSRF tokens again, remove said function call from the function.


## Using outdated components with known vulnerabilities (A9)
I downgraded Spring Boot in `pom.xml` to version *1.4.0* to force the use of outdated Maven dependencies.  
These can be found with the `owasp dependency-check-maven` tool that I also included in the `pom.xml`.  

**Steps to reproduce:**  
- Add the OWASP dependency-check-maven to `pom.xml` (see https://cybersecuritybase.github.io/securing/part3.html for more information)
- After updaing the Maven deps, call `mvn dependency-check:check`
- Observe the following output upon completion: 

```
One or more dependencies were identified with known vulnerabilities in cybersecuritybase-project:

logback-core-1.1.7.jar (ch.qos.logback:logback-core:1.1.7, cpe:/a:logback:logback:1.1.7) : CVE-2017-5929
jackson-core-2.8.1.jar (com.fasterxml.jackson.core:jackson-core:2.8.1, cpe:/a:fasterxml:jackson:2.8.1) : CVE-2016-7051
tomcat-embed-core-8.5.4.jar (cpe:/a:apache:tomcat:8.5.4, org.apache.tomcat.embed:tomcat-embed-core:8.5.4) : CVE-2017-7675, CVE-2017-7674, CVE-2017-6056, CVE-2017-5664, CVE-2017-5651, CVE-2017-5650, CVE-2017-5648, CVE-2017-5647, CVE-2017-12617, CVE-2016-8745, CVE-2016-8735, CVE-2016-6817, CVE-2016-6816, CVE-2016-6797, CVE-2016-6796, CVE-2016-6794, CVE-2016-6325, CVE-2016-5425, CVE-2016-5388, CVE-2016-5018, CVE-2016-0762
groovy-2.4.7.jar (cpe:/a:apache:groovy:2.4.7, org.codehaus.groovy:groovy:2.4.7) : CVE-2016-6497
spring-boot-starter-data-jpa-1.4.0.RELEASE.jar (cpe:/a:pivotal_software:spring_data_jpa:1.4.0, org.springframework.boot:spring-boot-starter-data-jpa:1.4.0.RELEASE) : CVE-2016-6652
spring-boot-1.4.0.RELEASE.jar (cpe:/a:pivotal:spring_framework:1.4.0, cpe:/a:pivotal_software:spring_framework:1.4.0, org.springframework.boot:spring-boot:1.4.0.RELEASE) : CVE-2016-9878
spring-core-4.3.2.RELEASE.jar (cpe:/a:pivotal:spring_framework:4.3.2, cpe:/a:pivotal_software:spring_framework:4.3.2, cpe:/a:springsource:spring_framework:4.3.2, cpe:/a:vmware:springsource_spring_framework:4.3.2, org.springframework:spring-core:4.3.2.RELEASE) : CVE-2016-9878
```

**How to fix:**
- After thoroughly reading the provided report in the `target` folder, update all referenced projects and plugins in the `pom.xml` to the latest version if possible.


## Unvalidated admin area exposed (A10)
I created an admin interface for the management of all currently open signups.  
Normally, one would place this behind a login or check of some kind.  
However, in this case, the admin panel is only "hidden" by not exposing links to it anywhere on the page - no further checking is implemented.  

**Steps to reproduce:**  
- Load the form at `/form` (i.e. http://localhost:8080/form)
- Add one or multiple signups in there (by filling name and address fields)
- Open the admin panel located at `/admin` (i.e. http://localhost:8080/admin)
- Observe all entries listed here with the option to delete any entry

**How to fix:**  
This is actually quite easy to fix. The easiest way would be to force a login before entering the /admin area.  
It would be even adviseable to introduce two groups: users and admins and checking the accounts' group membership before allowing access to the admin panel.  
For a base fix, it would be a good idea to add auth to admin.  
This is done by modifying the overriden method `configure` in `SecurityConfiguration.java` like so:  
```java
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin").authenticated();
        http.formLogin()
                .permitAll();
    }
```
This adds a login with the defined users (from `CustomUserDetailsService.java`) if one tries to open /admin.  
If you want to test this, log in with the user `admin` and the password `president`.
