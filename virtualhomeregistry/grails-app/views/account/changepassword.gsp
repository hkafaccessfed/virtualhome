<!doctype html>
<html>
  <head>
    <meta name="layout" content="myaccount" />
    <r:require modules="pwmask" />
  </head>
  <body>

    <h2>Hello <g:fieldValue bean="${managedSubjectInstance}" field="cn"/>! <small>Please supply the information requested to change your password</small></h2>

    <g:render template="/templates/flash" plugin="aafApplicationBase"/>
    <g:render template="/templates/errors_bean" model="['bean':managedSubjectInstance]" plugin="aafApplicationBase"/>

    <g:form action="completepasswordchange" class="form-horizontal myaccount form-validating" name="accountform">
      <div class="control-group">
        <label class="control-label" for="login">Username</label>
        <div class="controls">
          <div class="span5">
            <input class="required span5" name="login" id="login" type="text" autofocus="autofocus" autocomplete="off" value="${managedSubjectInstance.login}" disabled="true">
          </div>
          <div class="span4">
            <span class="help-block">
              <p><small><g:link action="logout">(This isn't me)</g:link></small></p>
            </span>
          </div>
        </div>
      </div>

      <hr>

      <div class="control-group">
        <label class="control-label" for="currentPassword">Current Password</label>
        <div class="controls">
          <div class="span5">
            <input class="span5 required" id="currentPassword" name="currentPassword" type="password" autocomplete="off">
          </div>
          <div class="span4">
            <span class="help-block">
              <p>Please enter the password you're currently using when logging into the AAF Virtual Home.</p>
            </span>
          </div>
        </div>
      </div>

      <hr>

      <g:render template="/templates/passwordinput"/>

      <div class="form-actions">
        <p class="text-muted"><i class="icon-info-sign"></i> <strong>Intensive cryptography coming up!! Your browser will take a few seconds to process the request after you click. There is no need to refresh.</strong></p>
        <button type="submit" class="btn btn-success btn-large">Change Password</button>
        <g:link action="show" class="btn btn-large"><g:message encodeAs='HTML' code="label.cancel"/></g:link>
      </div>
    </g:form>

  </body>
</html>
