<!doctype html>
<html>
  <head>
    <meta name="layout" content="myaccount" />
  </head>
  <body>

    <div class="row">
      <div class="span12">
        <h2>Hello <g:fieldValue bean="${managedSubjectInstance}" field="cn"/>!</h2>
      </div>
    </div>

    <div class="row">
      <div class="span12">
        <p>Your account has been configured to use 2-Step verification.</p>

        <div class="row"><div class="span12 centered"><br><img src="${totpURL}" width="200" height="200" class="img-polaroid"><br><br></div></div>

        <p>Please <strong>follow the instructions on your installed smart phone app</strong> to scan the above QR code and complete the setup on your phone.<p>
      </div>
    </div>

    <div class="row">
      <div class="span12">
        <p>Once your smart phone app is configured enter the current code below to continue logging into your service.</p>

        <g:form controller="login" action="twosteplogin" id="${managedSubjectInstance.id}" method="post" class="form form-login form-validating form-horizontal well well-small">
          <fieldset>
            <div class="control-group">
              <label class="control-label" for="totp"><g:message code="label.2stepcode"/></label>
              <div class="controls">
                <input id="totp" name="totp" type="text" autofocus="autofocus" class="required"/>
              </div>
            </div>

            <div class="control-group">
              <div class="controls">
                <button type="submit" value="Login" class="btn btn-large btn-info"><g:message code="label.completelogin"/></button>
              </div>
            </div>
          </fieldset>
        </g:form>
      </div>
    </div>

  </body>
</html>
