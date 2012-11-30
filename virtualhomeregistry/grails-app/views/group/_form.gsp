

<fieldset>

  <g:hiddenField name="organization.id" value="${groupInstance?.organization?.id}" />

  <div class="control-group ${hasErrors(bean: groupInstance, field: 'name', 'error')}">
    <label class="control-label" for="name"><g:message code="label.name"/></label>
    <div class="controls">
      <g:textField name="name" required="" value="${groupInstance?.name}"/>
      <a href="#" rel="tooltip" title="${g.message(code:'help.inline.aaf.vhr.group.name')}"><i class="icon icon-question-sign"></i></a>
    </div>
  </div>

  <div class="control-group ${hasErrors(bean: groupInstance, field: 'description', 'error')}">
    <label class="control-label" for="description"><g:message code="label.description"/></label>
    <div class="controls">
      <g:textField name="description" required="" value="${groupInstance?.description}"/>
      <a href="#" rel="tooltip" title="${g.message(code:'help.inline.aaf.vhr.group.description')}"><i class="icon icon-question-sign"></i></a>
    </div>
  </div>

</fieldset>
