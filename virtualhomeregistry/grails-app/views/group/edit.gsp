<html>
  <head>
    <meta name="layout" content="internal" />
  </head>
  <body>

    <ul class="breadcrumb">
      <li><g:link controller="dashboard"><g:message code="branding.application.name"/></g:link> <span class="divider">/</span></li>
      <li><g:link action="list"><g:message code="branding.nav.breadcrumb.group"/></g:link> <span class="divider">/</span></li>
      <li><g:link action="show" id="${groupInstance?.id}"><g:message code="branding.nav.breadcrumb.group.show"/></g:link> <span class="divider">/</span></li>
      <li class="active"><g:message code="branding.nav.breadcrumb.group.edit"/></li>
    </ul>

    <g:render template="/templates/flash" plugin="aafApplicationBase"/>
    <g:render template="/templates/errors_bean" model="['bean':groupInstance]" plugin="aafApplicationBase"/>

    <h2><g:message code="views.aaf.vhr.group.edit.heading" args="[]"/></h2>
    
    <g:form action="update" class="form-validating form-horizontal">
      <g:hiddenField name="id" value="${groupInstance?.id}" />
      <g:hiddenField name="version" value="${groupInstance?.version}" />
      <g:render template="form"/>
      <div class="form-actions">
        <button type="submit" class="btn btn-success"/><g:message code="label.update" /></button>
        <g:link class="btn" action="show" id="${groupInstance?.id}"><g:message code="label.cancel"/></g:link>
      </div>
    </g:form>
    
  </body>
</html>

