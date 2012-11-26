

<fieldset>

  <div class="control-group ${hasErrors(bean: groupInstance, field: 'name', 'error')}">
    <label class="control-label" for="name"><g:message code="label.name"/></label>
    <div class="controls">
      <g:textField name="name" required="" value="${groupInstance?.name}"/>
    </div>
  </div>

  <div class="control-group ${hasErrors(bean: groupInstance, field: 'description', 'error')}">
    <label class="control-label" for="description"><g:message code="label.description"/></label>
    <div class="controls">
      <g:textField name="description" required="" value="${groupInstance?.description}"/>
    </div>
  </div>

  <div class="control-group ${hasErrors(bean: groupInstance, field: 'active', 'error')}">
    <label class="control-label" for="active"><g:message code="label.active"/></label>
    <div class="controls">
      <g:checkBox name="active" value="${groupInstance?.active}" />
    </div>
  </div>

  <div class="control-group ${hasErrors(bean: groupInstance, field: 'organization', 'error')}">
    <label class="control-label" for="organization"><g:message code="label.organization"/></label>
    <div class="controls">
      <g:select id="organization" name="organization.id" from="${aaf.vhr.Organization.list()}" optionKey="id" required="" value="${groupInstance?.organization?.id}" class="many-to-one"/>
    </div>
  </div>

  <div class="control-group ${hasErrors(bean: groupInstance, field: 'subjects', 'error')}">
    <label class="control-label" for="subjects"><g:message code="label.subjects"/></label>
    <div class="controls">
        <ul class="one-to-many">
        <g:each in="${groupInstance?.subjects}" var="s">
         <li><g:link controller="managedSubject" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
        </g:each>
        <li class="add">
          <g:link controller="managedSubject" action="create" params="['group.id': groupInstance?.id]">${message(code: 'label.add')}</g:link>
        </li>
      </ul>
    </div>
  </div>

</fieldset>
