<html>
  <head>
    <meta name="layout" content="internal" />
  </head>
  <body>

    <ul class="breadcrumb">
      <li><g:link controller="dashboard"><g:message code="branding.application.name"/></g:link> <span class="divider">/</span></li>
      <li><g:link action="list"><g:message code="branding.nav.breadcrumb.organization"/></g:link> <span class="divider">/</span></li>
      <li class="active"><g:message code="branding.nav.breadcrumb.organization.create"/></li>
    </ul>

    <g:render template="/templates/flash" plugin="aafApplicationBase"/>
    <g:render template="/templates/errors_bean" model="['bean':organizationInstance]" plugin="aafApplicationBase"/>

    <h2><g:message code="views.aaf.vhr.organization.create.heading" /></h2>
    
    <g:form action="save" class="form-validating form-horizontal">
      <g:render template="form"/>
      <div class="form-actions">
        <button type="submit" class="btn btn-success"/><g:message code="label.create" /></button>
        <g:link class="btn" action="list"><g:message code="label.cancel"/></g:link>
      </div>
    </g:form>
    
  </body>
</html>

