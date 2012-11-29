
<html>
  <head>  
    <meta name="layout" content="internal" />
  </head>
  <body>

    <ul class="breadcrumb">
      <li><g:link controller="dashboard"><g:message code="branding.application.name"/></g:link> <span class="divider">/</span></li>
      <li class="active"><g:message code="branding.nav.breadcrumb.organization"/></li>
      
      <li class="pull-right"><strong><g:link action="create"><g:message code="branding.nav.breadcrumb.organization.create"/></g:link></strong></li>
    </ul>

    <g:render template="/templates/flash" plugin="aafApplicationBase"/>

    <h2><g:message code="views.aaf.vhr.organization.list.heading" /></h2>
    
      <table class="table table-borderless table-sortable">
        <thead>
          <tr>
              <th><g:message code="label.name" /></th> 
              <th><g:message code="label.displayname" /></th> 
              <th><g:message code="label.description" /></th> 
              <th><g:message code="label.active" /></th> 
              <th><g:message code="label.frid" /></th> 
              <th><g:message code="label.grouplimit" /></th> 
              <th/>
          </tr>
        </thead>
        <tbody>
        <g:each in="${organizationInstanceList}" status="i" var="organizationInstance">
          <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
            <td>${fieldValue(bean: organizationInstance, field: "name")}</td>
            <td>${fieldValue(bean: organizationInstance, field: "displayName")}</td>
            <td>${fieldValue(bean: organizationInstance, field: "description")}</td>
            <td><g:formatBoolean boolean="${organizationInstance.active}" /></td>
            <td>${fieldValue(bean: organizationInstance, field: "frID")}</td>
            <td>${fieldValue(bean: organizationInstance, field: "groupLimit")}</td>
            <td><g:link action="show" id="${organizationInstance.id}" class="btn btn-small"><g:message code="label.view"/></g:link></td>
          </tr>
        </g:each>
        </tbody>
      </table>
  </body>
</html>