<html>
  <body>

    <% 
    Cookie c; 

    c = new Cookie("_idp_session", null); 
    c.setPath("/idp"); 
    c.setMaxAge(0); 
    c.setSecure(true); 
    response.addCookie(c); 

    c = new Cookie("JSESSIONID", null); 
    c.setPath("/idp"); 
    c.setMaxAge(0); 
    c.setSecure(true); 
    response.addCookie(c); 

    c = new Cookie("vhr_login", null); 
    c.setPath("/"); 
    c.setMaxAge(0); 
    c.setSecure(true); 
    response.addCookie(c); 

    c = new Cookie("JSESSIONID", null); 
    c.setPath("/"); 
    c.setMaxAge(0); 
    c.setSecure(true); 
    response.addCookie(c); 

    session.invalidate(); 
    %>

    <h1>NOTICE</h1>
    <p>
      This URL has terminated cookies associated with a basic Shibboleth IdP session to assist with use of functional testing tools. 
    </p>
    <p>
      It is not considered to be a logout mechanism or officially supported by the Australian Access Federation.
    </p>
  </body>
</html>
