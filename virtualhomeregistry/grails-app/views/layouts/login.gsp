<!DOCTYPE html>
<html>
  <head>
    <title><g:message code='branding.application.name'/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="${resource(dir:'images', file:'logo.png')}" />
    
    <r:require modules="modernizr, bootstrap, bootstrap-responsive-css, validate, datatables, less, app"/>
    <r:layoutResources/>
    <g:layoutHead />

    <style id="antiClickjack">body{display:none !important;}</style>
    <script type="text/javascript">
       if (self === top) {
           var antiClickjack = document.getElementById("antiClickjack");
           antiClickjack.parentNode.removeChild(antiClickjack);

           $("#username").focus();
       } else {
           top.location = self.location;
       }
    </script>
  </head>

  <body>
    
    <header>      
      <g:render template='/templates/branding/header' />
    </header>

    <section>
      <div class="container">
        <div class='notifications top-right'></div>
        <g:layoutBody/>
      </div>
    </section>
  
    <footer>
      <div class="container"> 
        <div class="row">
          <div class="span12">
            <g:render template='/templates/branding/footer' />
          </div>
        </div>
      </div>
    </footer>

    <r:layoutResources/>
  </body>

</html>
