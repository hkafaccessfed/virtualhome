
<fieldset>

  <g:hiddenField name="group.id" value="${managedSubjectInstance.group.id}"/>
  <g:hiddenField name="eduPersonAssurance" value='urn:mace:aaf.edu.au:iap:id:1' />

  <h4><g:message code="label.coreattributes"/></h4>
  <div class="control-group ${hasErrors(bean: managedSubjectInstance, field: 'cn', 'error')}">
    <label class="control-label" for="cn"><g:message code="label.cn"/></label>
    <div class="controls">
      <g:textField name="cn" required="" value="${managedSubjectInstance?.cn}"/>

      <a href="#" rel="tooltip" title="${g.message(code:'help.inline.aaf.vhr.managedsubject.cn')}"><i class="icon icon-question-sign"></i></a>
    </div>
  </div>

  <div class="control-group ${hasErrors(bean: managedSubjectInstance, field: 'email', 'error')}">
    <label class="control-label" for="email"><g:message code="label.email"/></label>
    <div class="controls">
      <g:field type="email" name="email" required="" value="${managedSubjectInstance?.email}"/>

      <a href="#" rel="tooltip" title="${g.message(code:'help.inline.aaf.vhr.managedsubject.email')}"><i class="icon icon-question-sign"></i></a>
    </div>
  </div>

  <aaf:hasPermission target="app:administrator">
    <g:if test="${actionName != 'create'}">
      <div class="control-group ${hasErrors(bean: managedSubjectInstance, field: 'sharedToken', 'error')}">
        <label class="control-label" for="sharedToken"><g:message code="label.sharedtoken"/></label>
        <div class="controls">
          <g:textField name="sharedToken" required="" value="${managedSubjectInstance?.sharedToken}"/>

          <a href="#" rel="tooltip" title="${g.message(code:'help.inline.aaf.vhr.managedsubject.sharedtoken')}"><i class="icon icon-question-sign"></i></a>
        </div>
      </div>
    </g:if>
  </aaf:hasPermission>

  <div class="control-group ${hasErrors(bean: managedSubjectInstance, field: 'eduPersonAffiliation', 'error')}">
    <label class="control-label" for="eduPersonAffiliation"><g:message code="label.edupersonaffiliation"/></label>
    <div class="controls">
      <g:select name="eduPersonAffiliation" from="${managedSubjectInstance.affiliations}" 
      value="${managedSubjectInstance?.eduPersonAffiliation ?managedSubjectInstance?.eduPersonAffiliation.split(';') as List : 'member'}" 
      valueMessagePrefix="managedSubject.eduPersonAffiliation" multiple="multiple" id="edupersonaffiliation" required="required"/>

      <a href="#" rel="tooltip" title="${g.message(code:'help.inline.aaf.vhr.managedsubject.edupersonaffiliation')}"><i class="icon icon-question-sign"></i></a>
    </div>
  </div>

  <g:if test="${actionName == 'edit' || actionName == 'update'}">
    <div class="control-group ${hasErrors(bean: managedSubjectInstance, field: 'displayName', 'error')}">
      <label class="control-label" for="displayName"><g:message code="label.displayname"/></label>
      <div class="controls">
        <g:textField name="displayName" value="${managedSubjectInstance?.displayName}"/>

        <a href="#" rel="tooltip" title="${g.message(code:'help.inline.aaf.vhr.managedsubject.displayname')}"><i class="icon icon-question-sign"></i></a>
      </div>
    </div>

    <hr>

    <h4><g:message code="label.optionalattributes"/></h4>

    <div class="control-group ${hasErrors(bean: managedSubjectInstance, field: 'givenName', 'error')}">
      <label class="control-label" for="givenName"><g:message code="label.givenname"/></label>
      <div class="controls">
        <g:textField name="givenName" value="${managedSubjectInstance?.givenName}"/>

        <a href="#" rel="tooltip" title="${g.message(code:'help.inline.aaf.vhr.managedsubject.givenname')}"><i class="icon icon-question-sign"></i></a>
      </div>
    </div>

    <div class="control-group ${hasErrors(bean: managedSubjectInstance, field: 'surname', 'error')}">
      <label class="control-label" for="surname"><g:message code="label.surname"/></label>
      <div class="controls">
        <g:textField name="surname" value="${managedSubjectInstance?.surname}"/>

        <a href="#" rel="tooltip" title="${g.message(code:'help.inline.aaf.vhr.managedsubject.surname')}"><i class="icon icon-question-sign"></i></a>
      </div>
    </div>

    <div class="control-group ${hasErrors(bean: managedSubjectInstance, field: 'telephoneNumber', 'error')}">
      <label class="control-label" for="telephoneNumber"><g:message code="label.telephonenumber"/></label>
      <div class="controls">
        <g:textField name="telephoneNumber" value="${managedSubjectInstance?.telephoneNumber}"/>

        <a href="#" rel="tooltip" title="${g.message(code:'help.inline.aaf.vhr.managedsubject.telephonenumber')}"><i class="icon icon-question-sign"></i></a>
      </div>
    </div>

    <div class="control-group ${hasErrors(bean: managedSubjectInstance, field: 'mobileNumber', 'error')}">
      <label class="control-label" for="mobileNumber"><g:message code="label.mobilenumber"/></label>
      <div class="controls">
        <g:textField name="mobileNumber" value="${managedSubjectInstance?.mobileNumber}"/>

        <a href="#" rel="tooltip" title="${g.message(code:'help.inline.aaf.vhr.managedsubject.mobilenumber')}"><i class="icon icon-question-sign"></i></a>
      </div>
    </div>

    <div class="control-group ${hasErrors(bean: managedSubjectInstance, field: 'postalAddress', 'error')}">
      <label class="control-label" for="postalAddress"><g:message code="label.postaladdress"/></label>
      <div class="controls">
        <g:textField name="postalAddress" value="${managedSubjectInstance?.postalAddress}"/>

        <a href="#" rel="tooltip" title="${g.message(code:'help.inline.aaf.vhr.managedsubject.postaladdress')}"><i class="icon icon-question-sign"></i></a>
      </div>
    </div>

    <div class="control-group ${hasErrors(bean: managedSubjectInstance, field: 'organizationalUnit', 'error')}">
      <label class="control-label" for="organizationalUnit"><g:message code="label.organizationalunit"/></label>
      <div class="controls">
        <g:textField name="organizationalUnit" value="${managedSubjectInstance?.organizationalUnit}"/>

        <a href="#" rel="tooltip" title="${g.message(code:'help.inline.aaf.vhr.managedsubject.organizationalunit')}"><i class="icon icon-question-sign"></i></a>
      </div>
    </div>
  </g:if>

</fieldset>

<r:script>
  $(document).ready(function() {
    $('#edupersonaffiliation').multiselect();
  });
</r:script>
