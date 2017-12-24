# Cybersecurity Base - Project I

This is a fork of the base project for the first CSB project.  
Following is a writeup of all developer introduced bugs / [OWASP Top Ten](https://www.owasp.org/index.php/Top_10_2013-Top_10)  
  
---
  
## No CSRF Tokens
Has been set by using `http.csrf().disable();` in `SecurityConfiguration` (Should  be A8)  


## XSS in unescaped address input
This has been a bit more of trouble. Changed impl in `CyberSecurityBaseProjectApplication`.  
Also, there is now a list of all registered signups on the form with an unescaped span.  
This can be exploited by entering valid HTML in the `address` field of the signup form, i.e.  

    <script>alert(document.cookie)</script>


