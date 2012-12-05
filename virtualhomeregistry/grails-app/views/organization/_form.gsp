

<fieldset>

  <div class="control-group ${hasErrors(bean: organizationInstance, field: 'name', 'error')}">
    <label class="control-label" for="name"><g:message code="label.name"/></label>
    <div class="controls">
      <g:textField name="name" required="" value="${organizationInstance?.name}"/>
    </div>
  </div>

  <div class="control-group ${hasErrors(bean: organizationInstance, field: 'displayName', 'error')}">
    <label class="control-label" for="displayName"><g:message code="label.displayname"/></label>
    <div class="controls">
      <g:textField name="displayName" required="" value="${organizationInstance?.displayName}"/>
    </div>
  </div>

  <div class="control-group ${hasErrors(bean: organizationInstance, field: 'description', 'error')}">
    <label class="control-label" for="description"><g:message code="label.description"/></label>
    <div class="controls">
      <g:textArea name="description" cols="40" rows="5" maxlength="2000" value="${organizationInstance?.description}"/>
    </div>
  </div>

  <g:if test="${actionName == 'create'}">
    <div class="control-group ${hasErrors(bean: organizationInstance, field: 'frID', 'error')}">
      <label class="control-label" for="frID"><g:message code="label.frid"/></label>
      <div class="controls">
        <g:field name="frID" type="number" value="${organizationInstance.frID}" required=""/>
      </div>
    </div>
  </g:if>

  <aaf:hasPermission target='app:administration'>
    <div class="control-group ${hasErrors(bean: organizationInstance, field: 'groupLimit', 'error')}">
      <label class="control-label" for="groupLimit"><g:message code="label.grouplimit"/></label>
      <div class="controls">
        <g:field name="groupLimit" type="number" value="${organizationInstance.groupLimit}" required=""/>
      </div>
    </div>

    <div class="control-group ${hasErrors(bean: organizationInstance, field: 'subjectLimit', 'error')}">
      <label class="control-label" for="subjectLimit"><g:message code="label.subjectlimit"/></label>
      <div class="controls">
        <g:field name="subjectLimit" type="number" value="${organizationInstance.subjectLimit}" required=""/>
      </div>
    </div>
  </aaf:hasPermission>

</fieldset>
