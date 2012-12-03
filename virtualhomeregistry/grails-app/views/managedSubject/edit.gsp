<html>
  <head>
    <meta name="layout" content="internal" />
  </head>
  <body>

    <ul class="breadcrumb">
      <li><g:link controller="dashboard"><g:message code="branding.application.name"/></g:link> <span class="divider">/</span></li>
      <li><g:link action="list" controller="organization"><g:message code="branding.nav.breadcrumb.organization"/></g:link> <span class="divider">/</span></li>
      <li><g:link action="show" controller="organization" id="${managedSubjectInstance.group.organization.id}"><g:fieldValue bean="${managedSubjectInstance.group.organization}" field="displayName"/></g:link> <span class="divider">/</span></li>
      <li><g:link controller="group" action="list"><g:message code="branding.nav.breadcrumb.group"/></g:link> <span class="divider">/</span></li>
      <li><g:link controller="group" action="show" id="${managedSubjectInstance.group.id}"> <g:fieldValue bean="${managedSubjectInstance.group}" field="name"/></g:link> <span class="divider">/</span></li>
      <li><g:link action="list"><g:message code="branding.nav.breadcrumb.managedsubject"/></g:link> <span class="divider">/</span></li>
      <li><g:link action="show" id="${managedSubjectInstance.id}"><g:fieldValue bean="${managedSubjectInstance}" field="cn"/></g:link> <span class="divider">/</span></li>
      <li class="active"><g:message code="branding.nav.breadcrumb.managedsubject.edit"/></li>
    </ul>

    <g:render template="/templates/flash" plugin="aafApplicationBase"/>
    <g:render template="/templates/errors_bean" model="['bean':managedSubjectInstance]" plugin="aafApplicationBase"/>

    <h2><g:message code="views.aaf.vhr.managedsubject.edit.heading" args="${[managedSubjectInstance.cn]}"/></h2>
    
    <g:form action="update" class="form-validating form-horizontal">
      <g:hiddenField name="id" value="${managedSubjectInstance?.id}" />
      <g:hiddenField name="version" value="${managedSubjectInstance?.version}" />
      <g:render template="form"/>
      <div class="form-actions">
        <button type="submit" class="btn btn-success"/><g:message code="label.update" /></button>
        <g:link class="btn" action="show" id="${managedSubjectInstance?.id}"><g:message code="label.cancel"/></g:link>
      </div>
    </g:form>
    
  </body>
</html>

