<!doctype html>
<html>
  <head>
    <meta name="layout" content="myaccount" />
    <r:require modules="pwmask" />
  </head>
  <body>

    <div class="row">
      <div class="span6">
        <h2 class="muted">My Account</h2>

        <p>Welcome to the AAF Virtual Home account management portal.</p>
        <p>By logging in here you'll be able to:</p>
        <ul>
          <li>View your account details to assist with maintenance of your personal information;</li>
          <li>Manage your password; and</li>
          <li>Get details of who to contact for support with your account.</li>
        </ul>
      </div>

      <div class="span6">
        <div class="well well-small">            
          <g:if test="${loginError}">
            <div class="alert alert-block alert-error login-error">
              <p><strong>Unable to validate account or password</strong>.</p>
              <p>Please try again or use the account help links below.</p>
            </div>
          </g:if>

          <g:form action="login" method="post" class="form form-login form-validating">
            <h2>Login <span class="service-name"></span></h2>
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

          <h4 class="muted">How else can we help?</h4>
          <ul class="muted">
            <li><g:link controller="lostPassword" action="start">I've lost my password</g:link></li>
            <li><a href="http://support.aaf.edu.au/entries/22539753-I-m-collaborating-with-an-Institution-how-do-I-get-access-to-an-AAF-VH-account-">I need an account</a></li>
          </ul>
        </div>
      </div>
    </div>

  </body>
</html>
