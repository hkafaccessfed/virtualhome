
<html>
  <head>
    <meta name="layout" content="internal" />
  </head>

  <body>

    <ul class="breadcrumb">
      <li><g:link controller="dashboard"><g:message code="branding.application.name"/></g:link> <span class="divider">/</span></li>
      <li><g:link action="list" controller="organization"><g:message code="branding.nav.breadcrumb.organization"/></g:link> <span class="divider">/</span></li>
      <li><g:link action="show" controller="organization" id="${groupInstance.organization.id}"><g:fieldValue bean="${groupInstance.organization}" field="displayName"/></g:link> <span class="divider">/</span></li>
      <li><g:link action="list"><g:message code="branding.nav.breadcrumb.group"/></g:link> <span class="divider">/</span></li>
      <li><g:fieldValue bean="${groupInstance}" field="name"/></li>
    </ul>

    <g:render template="/templates/flash" plugin="aafApplicationBase"/>
    <g:render template="/templates/errors_bean" model="['bean':groupInstance]" plugin="aafApplicationBase"/>

    <h2><g:message code="views.aaf.vhr.group.show.heading" args="${[groupInstance.name]}"/></h2>

    <g:if test="${!groupInstance.functioning()}">
      <div class="alert alert-block alert-error">
        <h4><g:message code="views.aaf.vhr.group.show.functioning.heading"/></h4>
        <p><g:message code="views.aaf.vhr.group.show.functioning.reason"/></p>
        <p><g:message code="views.aaf.vhr.group.show.unable.to.login"/></p>
      </div>
    </g:if>

    <ul class="nav nav-tabs">
      <li class="active"><a href="#tab-overview" data-toggle="tab"><g:message code="label.overview" /></a></li>
      <li><a href="#tab-managedsubjects" data-toggle="tab"><g:message code="label.managedsubjects" /></a></li>

      <aaf:hasAnyPermission in='["app:manage:organization:${groupInstance.organization.id}:group:${groupInstance.id}:managedsubject:create, app:manage:organization:${groupInstance.organization.id}:group:${groupInstance.id}:edit","app:manage:organization:${groupInstance.organization.id}:group:${groupInstance.id}:delete"]'>
      <li class="dropdown pull-right">
        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
          <g:message code="label.actions" />
          <b class="caret"></b>
        </a>
        <ul class="dropdown-menu">
          <aaf:hasPermission target="app:manage:organization:${groupInstance.organization.id}:group:${groupInstance.id}:managedsubject:create">
            <li>
              <g:link action="create" controller="managedSubject" params='['group.id':"${groupInstance.id}"]'><g:message code="label.createmanagedsubject"/></g:link>
            </li>
            <li>
              <g:link action="create" controller="managedSubject" params='['group.id':"${groupInstance.id}"]'><g:message code="label.createmultiplemanagedsubject"/></g:link>
            </li>
            <li class="divider"></li>
          </aaf:hasPermission>

          <aaf:hasPermission target="app:manage:organization:${groupInstance.organization.id}:group:${groupInstance.id}:edit">
            <li>
              <g:form action="toggleActive" method="post">
                <g:hiddenField name="version" value="${groupInstance?.version}" />
                <g:hiddenField name="id" value="${groupInstance.id}" />
                <a href="#" onclick="$(this).parents('form').submit();">
                  <g:if test="${groupInstance.active}">
                    <g:message code="views.aaf.vhr.group.show.deactivate"/>
                  </g:if>
                  <g:else>
                    <g:message code="views.aaf.vhr.group.show.activate"/>
                  </g:else>
                </a>
              </g:form>
            </li>
            <li>
              <g:link action="edit" id="${groupInstance.id}"><g:message code="label.edit"/></g:link>
            </li>
          </aaf:hasPermission>

          <aaf:hasPermission target="app:manage:organization:${groupInstance.organization.id}:group:${groupInstance.id}:delete">
            <li>
              <a href="#" class="delete-ensure" data-confirm="${message(code:'views.aaf.vhr.group.confirm.remove')}"><g:message code="label.delete"/></a>
              <g:form action="delete" method="delete">
                <g:hiddenField name="id" value="${groupInstance.id}" />
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
              <td><span aria-labelledby="name-label"><g:fieldValue bean="${groupInstance}" field="name"/></span>
            </tr>
          
            <tr>
              <th class="span4"><span id="description-label"><strong><g:message code="label.description" /></strong></span></th>
              <td><span aria-labelledby="description-label"><g:fieldValue bean="${groupInstance}" field="description"/></span>
            </tr>

            <tr>
              <th class="span4"><span id="organization-label"><strong><g:message code="label.organization" /></strong></span></th>
              <td><span aria-labelledby="organization-label"><g:link controller="organization" action="show" id="${groupInstance?.organization?.id}"><g:fieldValue bean="${groupInstance.organization}" field="displayName"/></g:link></span>
            </tr>

            <tr><td colspan="2"><hr></tr></td>
            <tr><td colspan="2"><strong><g:message code="label.internaldata"/></tr></td>

            <tr>
              <th class="span4"><span id="internalid-label"><strong><g:message code="label.internalid" /></strong></span></th>
              <td><span aria-labelledby="internalid-label"><g:fieldValue bean="${groupInstance}" field="id" /></span>
            </tr>
          
            <tr>
              <th class="span4"><span id="datecreated-label"><strong><g:message code="label.datecreated" /></strong></span></th>
              <td><span aria-labelledby="datecreated-label"><g:formatDate date="${groupInstance?.dateCreated}" /></span>
            </tr>
          
            <tr>
              <th class="span4"><span id="lastupdated-label"><strong><g:message code="label.lastupdated" /></strong></span></th>
              <td><span aria-labelledby="lastupdated-label"><g:formatDate date="${groupInstance?.lastUpdated}" /></span>
            </tr>
          
            <tr>
              <th class="span4"><span id="active-label"><strong><g:message code="label.active" /></strong></span></th>
              <td><span aria-labelledby="active-label"><g:formatBoolean boolean="${groupInstance?.active}" /></span>
            </tr>
                    
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
          <g:each in="${groupInstance.subjects}" status="i" var="managedSubjectInstance">
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
