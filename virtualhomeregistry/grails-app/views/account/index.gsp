<!doctype html>
<html>
  <head>
    <meta name="layout" content="myaccount" />
    <r:require modules="pwmask" />
  </head>
  <body>

    <h2 class="muted">Hello! <small>To view your account please provide your username and password below</small></h2>

    <g:render template="/templates/flash" plugin="aafApplicationBase"/>

    <g:form action="show" class="form-horizontal myaccount form-validating">
      <div class="control-group">
        <label class="control-label" for="login">Username</label>
        <div class="controls">
          <div class="span5">
            <input class="required span5" name="login" id="login" type="text" autofocus="autofocus" autocomplete="off">
          </div>
          <div class="span4">
            <span class="help-block">
              <p>Please enter the username you selected when setting up your account.</p>
            </span>
          </div>
        </div>
      </div>

      <hr>

      <div class="control-group">
        <label class="control-label" for="plainPassword">Password</label><br>
        <div class="controls">
          <div class="span5">
            <input class="span5 required" id="plainPassword" name="plainPassword" type="password" autocomplete="off">
          </div>
          <div class="span4">
            <span class="help-block">
              <p>Please ensure you keep your password secure, you should not share it with anyone.</p>
            </span>
          </div>
        </div>
      </div>

      <div class="form-actions">
        <button type="submit" class="btn btn-info btn-large"><i class="icon-user icon-white"></i> Show me my account</button>
      </div>
    </g:form>

  </body>
</html>
