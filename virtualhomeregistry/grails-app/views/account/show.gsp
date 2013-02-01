<!doctype html>
<html>
  <head>
    <meta name="layout" content="myaccount" />
  </head>
  <body>

    <div class="row">
      <div class="span6">
        <h2>Hello <g:fieldValue bean="${managedSubjectInstance}" field="cn"/>!</h2>
      </div>
      <div class="span6">
        <div class="pull-right">
          <g:link action="changepassword" class="btn btn-info btn-large">Change your password</g:link>
        </div>
      </div>
    </div>

    <g:render template="/templates/flash" plugin="aafApplicationBase"/>

    <h3 class="muted">Your current account details <small style="font-size:0.5em;"><g:link action="logout">(This isn't me)</g:link></small></h3>
        
    <div class="row">
      <div class="span5">
        <table class="table table-borderless">
        <tbody>

          <tr><td colspan="2"><h4><g:message code="label.coreattributes"/></h4></tr></td>
          
          <tr>
            <th class="span4"><span id="cn-label"><strong><g:message code="label.cn" /></strong></span></th>
            <td><span aria-labelledby="cn-label"><g:fieldValue bean="${managedSubjectInstance}" field="cn"/></span></td>
          </tr>
        
          <tr>
            <th class="span4"><span id="email-label"><strong><g:message code="label.email" /></strong></span></th>
            <td><span aria-labelledby="email-label"><g:fieldValue bean="${managedSubjectInstance}" field="email"/></span></td>
          </tr>

          <tr>
            <th class="span4"><span id="displayname-label"><strong><g:message code="label.displayname" /></strong></span></th>
            <td><span aria-labelledby="displayname-label"><g:fieldValue bean="${managedSubjectInstance}" field="displayName"/></span></td>
          </tr>
        
          <tr>
            <th class="span4"><span id="edupersonassurance-label"><strong><g:message code="label.edupersonassurance" /></strong></span></th>
            <td><span aria-labelledby="edupersonassurance-label"><g:fieldValue bean="${managedSubjectInstance}" field="eduPersonAssurance"/></span></td>
          </tr>
        
          <tr>
              <th class="span4"><span id="edupersonaffiliation-label"><strong><g:message code="label.edupersonaffiliation" /></strong></span></th>
              <td>
                <span aria-labelledby="edupersonaffiliation-label">
                  <ol>
                  <g:each in="${managedSubjectInstance.eduPersonAffiliation.split(';')}" var='aff'>
                    <li>${aff.encodeAsHTML()}</li>
                  </g:each>
                  </ol>
                </span>
              </td>
            </tr>

          <tr>
            <th class="span4"><span id="edupersonentitlement-label"><strong><g:message code="label.edupersonentitlement" /></strong></span></th>
            <td>
              <span aria-labelledby="edupersonentitlement-label">
                <ol>
                <g:each in="${managedSubjectInstance.eduPersonEntitlement.split(';')}" var='ent'>
                  <li>${ent.encodeAsHTML()}</li>
                </g:each>
                </ol>
              </span>
            </td>
          </tr>

          <tr>
            <th class="span4"><span id="organization-label"><strong><g:message code="label.organization" /></strong></span></th>
            <td><span aria-labelledby="organization-label"><g:fieldValue bean="${managedSubjectInstance}" field="organization.displayName"/></span></td>
          </tr>

          <tr>
            <th class="span4"><span id="group-label"><strong><g:message code="label.group" /></strong></span></th>
            <td><span aria-labelledby="group-label"><g:fieldValue bean="${managedSubjectInstance}" field="group.name"/></span></td>
          </tr>
        </tbody>
        </table>
      </div>

      <div class="span5 offset2">
        <table class="table table-borderless">
        <tbody>
          <tr><td colspan="2"><h4><g:message code="label.optionalattributes"/></h4></tr></td>

          <tr>
            <th class="span4"><span id="givenname-label"><strong><g:message code="label.givenname" /></strong></span></th>
            <td><span aria-labelledby="givenname-label"><g:fieldValue bean="${managedSubjectInstance}" field="givenName"/></span>
          </tr>
        
          <tr>
            <th class="span4"><span id="surname-label"><strong><g:message code="label.surname" /></strong></span></th>
            <td><span aria-labelledby="surname-label"><g:fieldValue bean="${managedSubjectInstance}" field="surname"/></span>
          </tr>
        
          <tr>
            <th class="span4"><span id="telephonenumber-label"><strong><g:message code="label.telephonenumber" /></strong></span></th>
            <td><span aria-labelledby="telephonenumber-label"><g:fieldValue bean="${managedSubjectInstance}" field="telephoneNumber"/></span>
          </tr>

          <tr>
            <th class="span4"><span id="mobilenumber-label"><strong><g:message code="label.mobilenumber" /></strong></span></th>
            <td><span aria-labelledby="mobilenumber-label"><g:fieldValue bean="${managedSubjectInstance}" field="mobileNumber"/></span>
          </tr>
        
          <tr>
            <th class="span4"><span id="postaladdress-label"><strong><g:message code="label.postaladdress" /></strong></span></th>
            <td><span aria-labelledby="postaladdress-label"><g:fieldValue bean="${managedSubjectInstance}" field="postalAddress"/></span>
          </tr>
        
          <tr>
            <th class="span4"><span id="organizationalunit-label"><strong><g:message code="label.organizationalunit" /></strong></span></th>
            <td><span aria-labelledby="organizationalunit-label"><g:fieldValue bean="${managedSubjectInstance}" field="organizationalUnit"/></span>
          </tr>

        </tbody>
        </table>
      </div>
    </div>

    <h3 class="muted">Changing your account details</h3>
    <p>You can <g:link action="changepassword">change your password</g:link> using this website.</p>
    <p>In order to change your other details please contact one of the administrators of your account as shown below.</p>

    <h5 class="muted">Primary Administrators</h5>
    <table class="table table-borderless">
      <thead>
        <tr>
          <th><g:message code="label.name" /></th>
          <th><g:message code="label.email" /></th>
        </tr>
      </thead>
      <tbody>
        <g:each in="${groupRole.subjects?.sort{it.cn}}" var="subject">
          <tr>
            <td><g:fieldValue bean="${subject}" field="cn"/></td>
            <td><a href="mailto:${subject.email}"><g:fieldValue bean="${subject}" field="email"/></a></td>
          </tr>
        </g:each>
      </tbody>
    </table>

    <h5 class="muted">Secondary Administrators</h5>
    <table class="table table-borderless">
      <thead>
        <tr>
          <th><g:message code="label.name" /></th>
          <th><g:message code="label.email" /></th>
        </tr>
      </thead>
      <tbody>
        <g:each in="${organizationRole.subjects?.sort{it.cn}}" var="subject">
          <tr>
            <td><g:fieldValue bean="${subject}" field="cn"/></td>
            <td><a href="mailto:${subject.email}"><g:fieldValue bean="${subject}" field="email"/></a></td>
          </tr>
        </g:each>
      </tbody>
    </table>

    <div class="alert alert-info">
      <h5>Can't contact an administrator?</h5>
      <p>If you're unable to contact an account administator as shown above (or if there are no administrators shown) please get in touch with <a href="mailto:support@aaf.edu.au">AAF support at support@aaf.edu.au</a>. Please provide your username and details of who you've attempted to contact above as part of the email so we can assist you further.</p>
    </div>
  </body>
</html>
