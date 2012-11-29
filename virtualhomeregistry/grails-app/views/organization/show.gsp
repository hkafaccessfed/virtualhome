
<html>
  <head>
    <meta name="layout" content="internal" />
  </head>

  <body>

    <ul class="breadcrumb">
      <li><g:link controller="dashboard"><g:message code="branding.application.name"/></g:link> <span class="divider">/</span></li>
      <li><g:link action="list"><g:message code="branding.nav.breadcrumb.organization"/></g:link> <span class="divider">/</span></li>
      <li><g:fieldValue bean="${organizationInstance}" field="displayName"/></li>
    </ul>

    <g:render template="/templates/flash" plugin="aafApplicationBase"/>
    <g:render template="/templates/errors_bean" model="['bean':organizationInstance]" plugin="aafApplicationBase"/>

    <h2><g:message code="views.aaf.vhr.organization.show.heading" args="${[organizationInstance.displayName]}"/></h2>

    <g:if test="${organizationInstance.undergoingWorkflow}">
      <div class="alert alert-block alert-info">
        <h4><g:message code="views.aaf.vhr.organization.show.workflow.heading"/></h4>
        <p><g:message code="views.aaf.vhr.organization.show.workflow.reason"/></p>
        <p><g:message code="views.aaf.vhr.organization.show.unable.to.login"/></p>
      </div>
    </g:if>
    <g:if test="${!organizationInstance.functioning() && !organizationInstance.undergoingWorkflow}">
      <div class="alert alert-block alert-error">
        <h4><g:message code="views.aaf.vhr.organization.show.functioning.heading"/></h4>
        <p><g:message code="views.aaf.vhr.organization.show.functioning.reason"/></p>
        <p><g:message code="views.aaf.vhr.organization.show.unable.to.login"/></p>
      </div>
    </g:if>
    

    <ul class="nav nav-tabs">
      <li class="active"><a href="#tab-overview" data-toggle="tab"><g:message code="label.overview" /></a></li>
      <li><a href="#tab-groups" data-toggle="tab"><g:message code="label.groups" /></a></li>
      <li><a href="#tab-managedsubjects" data-toggle="tab"><g:message code="label.managedsubjects" /></a></li>

      <aaf:hasAnyPermission in='["app:manage:organization:${organizationInstance.id}:edit","app:manage:organization:${organizationInstance.id}:delete"]'>
        <li class="dropdown pull-right">

          <a class="dropdown-toggle" data-toggle="dropdown" href="#">
            <g:message code="label.actions" />
            <b class="caret"></b>
          </a>
          
          <ul class="dropdown-menu">
            <li>
              <g:link action="create" controller="group" params='['organization.id':"${organizationInstance.id}"]'><g:message code="label.creategroup"/></g:link>
            </li>
            <aaf:hasPermission target="app:manage:organization:${organizationInstance.id}:edit">
              <li>
                <g:link action="edit" id="${organizationInstance.id}"><g:message code="label.edit"/></g:link>
              </li>
            </aaf:hasPermission>
            <aaf:hasPermission target="app:manage:organization:${organizationInstance.id}:delete">
              <li>
                <a href="#" class="delete-ensure" data-confirm="${message(code:'views.aaf.vhr.organization.confirm.remove')}"><g:message code="label.delete"/></a>
                <g:form action="delete" method="delete">
                  <g:hiddenField name="id" value="${organizationInstance.id}" />
                </g:form>
              </li>
            </aaf:hasPermission>
          </ul>
        </li>
      </aaf:hasAnyPermission>
    </ul>

    <div class="tab-content">
      <div id="tab-overview" class="tab-pane active">
        <table class="table table-borderless">
          <tbody>
            
            <tr>
              <th class="span4"><span id="name-label"><strong><g:message code="label.name" /></strong></span></th>
              <td><span aria-labelledby="name-label"><g:fieldValue bean="${organizationInstance}" field="name"/></span>
            </tr>
          
            <tr>
              <th class="span4"><span id="displayname-label"><strong><g:message code="label.displayname" /></strong></span></th>
              <td><span aria-labelledby="displayname-label"><g:fieldValue bean="${organizationInstance}" field="displayName"/></span>
            </tr>
          
            <tr>
              <th class="span4"><span id="description-label"><strong><g:message code="label.description" /></strong></span></th>
              <td><span aria-labelledby="description-label"><g:fieldValue bean="${organizationInstance}" field="description"/></span>
            </tr>
          
            <tr>
              <th class="span4"><span id="grouplimit-label"><strong><g:message code="label.grouplimit" /></strong></span></th>
              <td>
                <g:if test="${organizationInstance.subjectLimit > 0}"
                  <span aria-labelledby="grouplimit-label"><g:fieldValue bean="${organizationInstance}" field="groupLimit"/></span>
                </g:if>
                <g:else>
                  <g:message code="label.unlimited"/>
                </g:else>
            </tr>
          
            <tr>
              <th class="span4"><span id="subjectlimit-label"><strong><g:message code="label.subjectlimit" /></strong></span></th>
              <td>
                <g:if test="${organizationInstance.subjectLimit > 0}"
                  <span aria-labelledby="subjectlimit-label"><g:fieldValue bean="${organizationInstance}" field="subjectLimit"/></span>
                </g:if>
                <g:else>
                  <g:message code="label.unlimited"/>
                </g:else>
            </tr>

            <tr><td colspan="2"><hr></tr></td>
            <tr><td colspan="2"><strong><g:message code="label.internaldata"/></tr></td>

            <tr>
              <th class="span4"><span id="internalid-label"><strong><g:message code="label.internalid" /></strong></span></th>
              <td><span aria-labelledby="internalid-label"><g:fieldValue bean="${organizationInstance}" field="id" /></span>
            </tr>

            <tr>
              <th class="span4"><span id="active-label"><strong><g:message code="label.active" /></strong></span></th>
              <td><span aria-labelledby="active-label"><g:formatBoolean boolean="${organizationInstance?.active}" /></span>
            </tr>

            <tr>
              <th class="span4"><span id="frid-label"><strong><g:message code="label.frid" /></strong></span></th>
              <td><span aria-labelledby="frid-label"><g:fieldValue bean="${organizationInstance}" field="frID"/></span>
            </tr>

            <tr>
              <th class="span4"><span id="datecreated-label"><strong><g:message code="label.datecreated" /></strong></span></th>
              <td><span aria-labelledby="datecreated-label"><g:formatDate date="${organizationInstance?.dateCreated}" /></span>
            </tr>
          
            <tr>
              <th class="span4"><span id="lastupdated-label"><strong><g:message code="label.lastupdated" /></strong></span></th>
              <td><span aria-labelledby="lastupdated-label"><g:formatDate date="${organizationInstance?.lastUpdated}" /></span>
            </tr>
          
          </tbody>
        </table>
      </div>

      <div id="tab-groups" class="tab-pane">        
        <table class="table table-borderless table-sortable">
          <thead>
            <tr>
              <th><g:message code="label.name" /></th> 
              <th><g:message code="label.description" /></th> 
              <th><g:message code="label.active" /></th> 
              <th/>
            </tr>
          </thead>
          <tbody>
          <g:each in="${organizationInstance.groups}" status="i" var="groupInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
              <td>${fieldValue(bean: groupInstance, field: "name")}</td>
              <td>${fieldValue(bean: groupInstance, field: "description")}</td>
              <td><g:formatBoolean boolean="${groupInstance.active}" /></td>
              <td><g:link action="show" controller="group" id="${groupInstance.id}" class="btn btn-small"><g:message code="label.view"/></g:link></td>
            </tr>
          </g:each>
          </tbody>
        </table>
      </div>

      <div id="tab-managedsubjects" class="tab-pane">
        <table class="table table-borderless table-sortable">
          <thead>
            <tr>
                <th><g:message code="label.login" /></th> 
                <th><g:message code="label.cn" /></th> 
                <th><g:message code="label.email" /></th> 
                <th/>
            </tr>
          </thead>
          <tbody>
          <g:each in="${organizationInstance.subjects}" status="i" var="managedSubjectInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
              <td>${fieldValue(bean: managedSubjectInstance, field: "login")}</td>
              <td>${fieldValue(bean: managedSubjectInstance, field: "cn")}</td>
              <td>${fieldValue(bean: managedSubjectInstance, field: "email")}</td>
              <td><g:link action="show" controller="managedSubject" id="${managedSubjectInstance.id}" class="btn btn-small"><g:message code="label.view"/></g:link></td>
            </tr>
          </g:each>
          </tbody>
        </table>
      </div>

    </div>

  </body>
</html>
