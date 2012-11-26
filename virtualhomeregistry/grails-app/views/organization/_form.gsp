

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

  <div class="control-group ${hasErrors(bean: organizationInstance, field: 'active', 'error')}">
    <label class="control-label" for="active"><g:message code="label.active"/></label>
    <div class="controls">
      <g:checkBox name="active" value="${organizationInstance?.active}" />
    </div>
  </div>

  <div class="control-group ${hasErrors(bean: organizationInstance, field: 'frID', 'error')}">
    <label class="control-label" for="frID"><g:message code="label.frid"/></label>
    <div class="controls">
      <g:field name="frID" type="number" value="${organizationInstance.frID}" required=""/>
    </div>
  </div>

  <div class="control-group ${hasErrors(bean: organizationInstance, field: 'groupLimit', 'error')}">
    <label class="control-label" for="groupLimit"><g:message code="label.grouplimit"/></label>
    <div class="controls">
      <g:field name="groupLimit" type="number" value="${organizationInstance.groupLimit}" required=""/>
    </div>
  </div>

  <div class="control-group ${hasErrors(bean: organizationInstance, field: 'groups', 'error')}">
    <label class="control-label" for="groups"><g:message code="label.groups"/></label>
    <div class="controls">
        <ul class="one-to-many">
        <g:each in="${organizationInstance?.groups}" var="g">
         <li><g:link controller="group" action="show" id="${g.id}">${g?.encodeAsHTML()}</g:link></li>
        </g:each>
        <li class="add">
          <g:link controller="group" action="create" params="['organization.id': organizationInstance?.id]">${message(code: 'label.add')}</g:link>
        </li>
      </ul>
    </div>
  </div>

  <div class="control-group ${hasErrors(bean: organizationInstance, field: 'subjectLimit', 'error')}">
    <label class="control-label" for="subjectLimit"><g:message code="label.subjectlimit"/></label>
    <div class="controls">
      <g:field name="subjectLimit" type="number" value="${organizationInstance.subjectLimit}" required=""/>
    </div>
  </div>

  <div class="control-group ${hasErrors(bean: organizationInstance, field: 'subjects', 'error')}">
    <label class="control-label" for="subjects"><g:message code="label.subjects"/></label>
    <div class="controls">
        <ul class="one-to-many">
        <g:each in="${organizationInstance?.subjects}" var="s">
         <li><g:link controller="managedSubject" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
        </g:each>
        <li class="add">
          <g:link controller="managedSubject" action="create" params="['organization.id': organizationInstance?.id]">${message(code: 'label.add')}</g:link>
        </li>
      </ul>
    </div>
  </div>

  <div class="control-group ${hasErrors(bean: organizationInstance, field: 'undergoingWorkflow', 'error')}">
    <label class="control-label" for="undergoingWorkflow"><g:message code="label.undergoingworkflow"/></label>
    <div class="controls">
      <g:checkBox name="undergoingWorkflow" value="${organizationInstance?.undergoingWorkflow}" />
    </div>
  </div>

</fieldset>
