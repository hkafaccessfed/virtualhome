<!doctype html>
<html>
  <head>
    <meta name="layout" content="internal" />
    <r:require modules="pwmask" />
  </head>
  <body>

    <div class="row">
      <div class="span12">
       <div class="page-header">
        <h1>Welcome <g:fieldValue bean="${managedSubjectInstance}" field="cn"/> <small> Your account is nearly ready....</small></h1>
        <p class="lead">Please choose a username, a <strong>secure</strong> password and provide your mobile phone number below.</p>
      </div>

      <g:render template="/templates/flash" plugin="aafApplicationBase"/>
      <g:render template="/templates/errors_bean" model="['bean':managedSubjectInstance]" plugin="aafApplicationBase"/>

      

      <g:form controller="finalization" action="complete" class="form-horizontal finalize" name="finalizeform">
        <g:hiddenField name="inviteCode" value="${invitationInstance.inviteCode}"/>

        <div class="control-group">
          <label class="control-label" for="login">Username</label>
          <div class="controls">
            <div class="span5">
              <input class="required span5" name="login" id="login" type="text" autofocus="autofocus" autocomplete="off" value="${managedSubjectInstance.login}">
            </div>
            <div class="span4">
              <span class="help-block">
                <p>You can choose anything you like for your username so long as:
                  <ul>
                    <li>It is memorable for you;</li>
                    <li>It is between 3 and 255 characters in length; and</li>
                    <li>Contains no whitespace.</li>
                  </ul>
                </p>
                <br><br>
              </span>
            </div>
          </div>
        </div>

        <hr>

        <div class="control-group">
          <label class="control-label" for="plainPassword">Password</label><br>
          <div class="controls">
            <div class="span5">
              <input class="span5 required" id="plainPassword" name="plainPassword" type="password" autocomplete="off" data-typetoggle="#showpassword">
              <input id="showpassword" type="checkbox" tabindex="-1"/> <span>unmask</span>
              <p class="lead" style="margin-top:12px; margin-bottom: 6px;">Your password is <strong id="pwlength">0</strong> characters long</p>
            </div>
            <div class="span4">
              <span class="help-block">
                <p>The AAF has strict password requirements in order to meet an international security standard called NIST 800-63.</p>
                <p>Please choose a password that meets the following requirements:</p>
                <ul>
                  <li>Does not contain your username;</li>
                  <li>Minimum <strong>length of 8 characters</strong>;</li>
                  <li>No whitespace;</li>
                  <li>No alphabetic sequences;</li>
                  <li>No numerical sequences;</li>
                  <li>No QWERTY sequences; and</li>
                  <li>No more than 3 repeat characters.</li>
                </ul>
                <p>The AAF <a href="http://xkcd.com/936/" target="_blank" tabindex="-1">and others</a> recommend you use a random password (passphrase) of <strong>greater then 16 characters</strong>. A phrase unique to you is the easiest way to achieve this: e.g. <em>'IbackedHorsenumber46intheMelbournecup!' or 'ilovedvisitingIcelandin2012'</em>. Don't use a famous quotation from literature. Capitalisation and non alphabetic characters as shown make your phrase even better.
                <p>If you wish to use a password between 8 and 16 characters in length the following requirements <strong>also apply</strong>:</p>
                <ul>
                  <li>At least 1 number</li>
                  <li>At least 1 Non Alpha</li>
                  <li>At least 1 Uppercase Character</li>
                  <li>At least 1 Lowercase Chatacter</li>
                  <li>No dictionary words</li>
                </ul>
              </span>
            </div>
          </div>
        </div>

        <div class="control-group">
          <label class="control-label" for="plainPasswordConfirmation">Confirm Password</label>
          <div class="controls">
            <div class="span5">
              <input class="span5 required" name="plainPasswordConfirmation" id="plainPasswordConfirmation" type="password" autocomplete="off" data-typetoggle="#showpassword">
            </div>
            <div class="span4">
              <span class="help-block">Please re-type your password so we can make sure you didn't make a mistake</span>
            </div>
          </div>
        </div>

        <hr>

        <div class="control-group">
          <label class="control-label" for="plainPasswordConfirmation">Mobile Number</label>
          <div class="controls">
            <div class="span5">
              <input class="span5" name="mobileNumber" type="text" autocomplete="off" value="${managedSubjectInstance.mobileNumber}">
            </div>
            <div class="span4">
              <div class="help-block">
                <p>Please provide your mobile number.</p>
                <p>We <strong>only use</strong> your mobile number to assist in ensuring security if you forget your password and need to reset it.</p>
                <p>International numbers are supported. If you're located outside Australia please be sure to add your <a href="http://en.wikipedia.org/wiki/List_of_mobile_phone_number_series_by_country" target="_blank">international prefix</a> to the start of your number.
                <p>If you elect not to supply your mobile number and forget your password you'll need to manually contact administrators which may take some time to complete preventing your access to AAF connected services in the interim.</p>
              </div>
            </div>
          </div>
        </div>


        <div class="form-actions">
          <p class="text-muted"><i class="icon-info-sign"></i> <strong>Intensive cryptography coming up!! Your browser will take a few seconds to talk with VHR after you click, there is no need to refresh.</strong></p>

          <button type="submit" class="btn btn-info btn-large"><i class="icon-user icon-white"></i> Finalise your account</button>
          
        </div>
      </g:form>

      </div>
    </div>

    <r:script>
      $('#plainPassword').showPassword();
      $('#plainPasswordConfirmation').showPassword();

      var examples = ['lol', 'correcthorsebatterystaple', 'ilovedvisitingIcelandin2012', 'IbackedHorsenumber46intheMelbournecup!'];

      $('#plainPassword').on("keyup", function() {
        $('#pwlength').html($('#plainPassword').val().length);
        
      });

      $('button[type=submit]').click(function() {
        if($("#finalizeform").valid()) {
          aaf_base.set_button($(this));
          $(this).parents('form').submit();
        }
      });

      jQuery.validator.addMethod("notAnExample", function(value, element) {
        if(jQuery.inArray($('#plainPassword').val().trim(),examples) != -1) {
          return false;
        } else {
          return true;
        }
      }, "");

      jQuery.validator.addMethod("noSpace", function(value, element) { 
        return value.indexOf(" ") < 0 && value != ""; 
      }, "Spaces are not permitted.");

      $("#finalizeform").validate({
        rules: {
          login: {
            required: true,
            minlength: 3,
            noSpace:true,
            remote: { url:"login-available", async:true }
          },
          plainPassword: {
            minlength: 8,
            notAnExample: true
          },
          plainPasswordConfirmation: {
            minlength: 8,
            equalTo: '#plainPassword'
          }
        },
        errorElement: "div",
        wrapper: "div",
        errorPlacement: function(error, element) {
          error.css('margin-bottom', '12px');

          error.addClass('text-error');
          element.parent().append(error);

          if(!$('#loginavailable').hasClass('hidden')) {
            $('#loginavailable').addClass('hidden');
          }
        },
        messages:{
          login: {
            remote: 'Unfortunately <strong>this username is taken</strong>, please choose something unique.'
          },
          plainPassword: {
            notAnExample: '<strong>LOL!!!</strong> not a good idea to use the examples :)'
          },
          plainPasswordConfirmation: {
            equalTo: 'Oops! Your passwords do not quite match.'
          }
        },
        keyup: true,
        focusout: false,
        focusInvalid:true
      });

    </r:script>
  </body>
</html>
