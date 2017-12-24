# Cybersecurity Base - Project I

This is a fork of the base project for the first CSB project.  
Following is a writeup of all developer introduced bugs / [OWASP Top Ten](https://www.owasp.org/index.php/Top_10_2013-Top_10)  
  
---
  
## No CSRF Tokens (A8)
Has been set by using `http.csrf().disable();` in `SecurityConfiguration` (Should  be A8)  


## XSS in unescaped address input (A3)
This has been a bit more of trouble. Changed impl in `CyberSecurityBaseProjectApplication`.  
Also, there is now a list of all registered signups on the form with an unescaped span.  
This can be exploited by entering valid HTML in the `address` field of the signup form, i.e.  

    <script>alert(document.cookie)</script>


## Unvalidated admin area exposed (A10)
There is an admin panel where existing signups can be removed.  
Norally, one would implement some kind of login / authorization to prevent every user of the site from using this.  
As one can think, this hasn't been done here. One can find the admin panel at [/admin](http://localhost:8080/admin).  
(FYI: This page will be empty at start - please add some signups at [/form](http://localhost:8080/form)!)


## Using outdated components with known vulnerabilities (A9)
I downgraded Spring Boot in `pom.xml` to version *1.4.0* to force the use of outdated Maven dependencies.  
These can be found with the `owasp dependency-check-maven` tool that I also included in the `pom.xml`.  
Calling the now added Maven task `dependency-check:check` (see [Securing Software Part 3](https://cybersecuritybase.github.io/securing/part3.html)) yields the following results:  

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
