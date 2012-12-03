
<html>
  <head>  
    <meta name="layout" content="internal" />
  </head>
  <body>
    <ul class="breadcrumb">
      <li><g:link controller="dashboard"><g:message code="branding.application.name"/></g:link> <span class="divider">/</span></li>
      <li class="active"><g:message code="branding.nav.breadcrumb.managedsubject"/></li>
    </ul>

    <g:render template="/templates/flash" plugin="aafApplicationBase"/>

    <h2><g:message code="views.aaf.vhr.managedsubject.list.heading" /></h2>
    
      <table class="table table-borderless table-sortable">
        <thead>
          <tr>
            <th><g:message code="label.login" /></th> 
            <th><g:message code="label.cn" /></th> 
            <th><g:message code="label.email" /></th> 
            <th><g:message code="label.sharedtoken" /></th>
            <th><g:message code="label.functioning" /></th> 
            <th><g:message code="label.group" /></th> 
            <th/>
          </tr>
        </thead>
        <tbody>
        <g:each in="${managedSubjectInstanceList}" status="i" var="managedSubjectInstance">
          <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
            <td>${fieldValue(bean: managedSubjectInstance, field: "login")}</td>
            <td>${fieldValue(bean: managedSubjectInstance, field: "cn")}</td>
            <td>${fieldValue(bean: managedSubjectInstance, field: "email")}</td>  
            <td>${fieldValue(bean: managedSubjectInstance, field: "sharedToken")}</td>
            <td><g:formatBoolean boolean="${managedSubjectInstance.functioning()}"/></td>
            <td><g:link action="show" controller="group" id="${managedSubjectInstance.group.id}">${fieldValue(bean: managedSubjectInstance, field: "group.name")}</g:link></td>
            <td><g:link action="show" id="${managedSubjectInstance.id}" class="btn btn-small"><g:message code="label.view"/></g:link></td>
          </tr>
        </g:each>
        </tbody>
      </table>
  </body>
</html>
