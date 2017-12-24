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