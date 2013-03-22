<!doctype html>
<html>
  <head>
    <meta name="layout" content="login" />
  </head>
  <body>

      <div class="row">
        <div id="servicedescription" class="span6">
          <h2>Welcome to the new AAF Virtual Home</h2>
          <p>Over the past few months we've been hard at work on this new version of the AAF Virtual Home.</p>
          <p> The new version will enable small organisations, including collaborative research facilities, to manage user identities for international, government and industry based researchers.</p>

          <p>Users are provided with:</p>
          <ul>
            <li>A view of account details to assist with maintenance of your personal information;</li>
            <li>Secure management of passwords;</li>
            <li>The ability to reset a forgotten password 24/7 with our simple online tools; and</li>
            <li>Support for multiple browsers and mobile devices to assist you logging in wherever you are.</li>
          </ul>

          <p>Administrators are provided with:</p>
          <ul>
            <li>An easier to use and navigate administrative interface;</li>
            <li>Per organisation and per group scopes for authentication assertions;
            <li>Better security for your users. Password resets use one time codes instead of having you set something on their behalf; and</li>
            <li>The ability to manage many more groups and users than was previously possible.</li>
          </ul>

          <br><br>
          <center><r:img file="loginlogos.png" width="297px" height="304px"/></center>
        </div>

        <div id="loginform" class="span5 offset1">

          <div class="well well-small">
            <h2>Login <span class="service-name"></span></h2>
            
            <g:if test="${loginError}">
              <div class="alert alert-block alert-error login-error">
                <p><strong>Unable to validate account or password</strong>.</p>
                <p>Please try again or use the account help links below.</p>
              </div>
            </g:if>
            <g:form action="login" method="post" class="form form-login form-validating">
              <fieldset>
                <div class="control-group">
                  <label class="control-label" for="username">Username</label>
                  <div class="controls">
                    <input id="username" name="username" type="text" autofocus="autofocus" class="required"/>
                  </div>
                </div>

                <div class="control-group">
                  <label class="control-label" for="password">Password</label>
                  <div class="controls">
                    <input id="password" name="password" type="password" class="required"/>
                  </div>
                </div>
                
                <g:if test="${requiresChallenge}">
                  <div class="control-group">
                    <label class="control-label" for="password">Challenge Response</label>
                    <div class="controls">
                      <recaptcha:ifEnabled>
                        <recaptcha:recaptcha theme="white" class="required"/>
                      </recaptcha:ifEnabled>
                      <p class="">
                        <small>
                          Due to repeated errors please provide the above
                          challenge response answers <strong>in addition to your username and password</strong>
                          so we can validate your account.
                        </small>
                      </p>
                    </div>
                    
                  </div>
                  <br>
                </g:if>

                <button type="submit" value="Login" class="btn btn-large btn-info">Login</button>
              
              </fieldset>
            </g:form>

            <hr>

            <h4 class="muted">How else can we help?</h4>
            <ul class="muted">
              <li><g:link controller="account" action="index">I'd like to view my account and <strong>get support contact details</strong></g:link></li>
              <li><g:link controller="lostPassword" action="start">I've lost my password</g:link></li>
              <li><a href="http://support.aaf.edu.au/categories/20072053-end-users-and-researchers">I need an account</a></li>
            </ul>
          </div>
        </div>
      </div>

      <r:script type="text/javascript">
      $(function () {
        extend_media_queries();
        $("#username").focus();

        //$.getJSON('/virtualhomeregistry/login/servicedetails?id=539', function(data) {
        //  $(".service-name").html("to " + data.serviceprovider.name);
        //});
      });

      $(window).resize(function() {
        extend_media_queries();
      });

      // IE[6|7|8] sucks, this helps us with responsive content cross browser
      // Also effectively turns off fluid grid for tablets
      function extend_media_queries() {
        if ($(window).width() >= 980) {
          $("#loginform").removeClass('span12');
          $("#loginform").addClass('span5 offset1');
          $("#servicedescription").removeClass('hidden');
        } else {
          $("#loginform").removeClass('span5 offset1');
          $("#servicedescription").addClass('hidden');
          $("#loginform").addClass('span12');
        }
      };
    </r:script>
  </body>
</html>
