
<html>
  <head>  
    <meta name="layout" content="internal" />
  </head>
  <body>

    <ul class="breadcrumb">
      <li><g:link controller="dashboard"><g:message code="branding.application.name"/></g:link> <span class="divider">/</span></li>
      <li class="active"><g:message code="branding.nav.breadcrumb.group"/></li>
    </ul>

    <g:render template="/templates/flash" plugin="aafApplicationBase"/>

    <h2><g:message code="views.aaf.vhr.group.list.heading" /></h2>
    
    <table class="table table-borderless table-sortable">
      <thead>
        <tr>
          <th><g:message code="label.name" /></th> 
          <th><g:message code="label.description" /></th> 
          <th><g:message code="label.organization" /></th> 
          <th><g:message code="label.active" /></th> 
          <th><g:message code="label.archived" /></th> 
          <th/>
        </tr>
      </thead>
      <tbody>
      <g:each in="${groupInstanceList.sort{it.name}}" status="i" var="groupInstance">
        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
          <td>${fieldValue(bean: groupInstance, field: "name")}</td>
          <td>${fieldValue(bean: groupInstance, field: "description")}</td>
          <td>${fieldValue(bean: groupInstance, field: "organization.displayName")}</td>
          <td><g:formatBoolean boolean="${groupInstance.active}" /></td>
          <td><g:formatBoolean boolean="${groupInstance.archived}" /></td>
          <td><g:link action="show" id="${groupInstance.id}" class="btn btn-small"><g:message code="label.view"/></g:link></td>
        </tr>
      </g:each>
      </tbody>
    </table>
  </body>
</html>
