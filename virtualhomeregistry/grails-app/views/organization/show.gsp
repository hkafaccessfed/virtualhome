
<html>
  <head>
    <meta name="layout" content="internal" />
  </head>

  <body>

    <ul class="breadcrumb">
      <li><g:link controller="dashboard"><g:message code="branding.application.name"/></g:link> <span class="divider">/</span></li>
      <li><g:link action="list"><g:message code="branding.nav.breadcrumb.organization"/></g:link> <span class="divider">/</span></li>
      <li><g:message code="branding.nav.breadcrumb.organization.show"/></li>
    </ul>

    <g:render template="/templates/flash" plugin="aafApplicationBase"/>
    <g:render template="/templates/errors_bean" model="['bean':organizationInstance]" plugin="aafApplicationBase"/>

    <h2><g:message code="views.aaf.vhr.organization.show.heading" args="[]"/></h2>

    <ul class="nav nav-tabs">
      <li class="active"><a href="#tab-overview" data-toggle="tab"><g:message code="label.overview" /></a></li>
      <li class="dropdown pull-right">
        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
          <g:message code="label.actions" />
          <b class="caret"></b>
        </a>
        <ul class="dropdown-menu">
          <li>
            <g:link action="edit" id="${organizationInstance.id}"><g:message code="label.edit"/></g:link>
            <a href="#" class="delete-ensure" data-confirm="${message(code:'views.aaf.vhr.organization.confirm.remove')}"><g:message code="label.delete"/></a>
            <g:form action="delete" method="delete">
              <g:hiddenField name="id" value="${organizationInstance.id}" />
            </g:form>
          </li>
        </ul>
      </li>
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
              <th class="span4"><span id="datecreated-label"><strong><g:message code="label.datecreated" /></strong></span></th>
              <td><span aria-labelledby="datecreated-label"><g:formatDate date="${organizationInstance?.dateCreated}" /></span>
            </tr>
          
            <tr>
              <th class="span4"><span id="lastupdated-label"><strong><g:message code="label.lastupdated" /></strong></span></th>
              <td><span aria-labelledby="lastupdated-label"><g:formatDate date="${organizationInstance?.lastUpdated}" /></span>
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
              <th class="span4"><span id="grouplimit-label"><strong><g:message code="label.grouplimit" /></strong></span></th>
              <td><span aria-labelledby="grouplimit-label"><g:fieldValue bean="${organizationInstance}" field="groupLimit"/></span>
            </tr>
          
            <tr>
              <th class="span4"><span id="groups-label"><strong><g:message code="label.groups" /></strong></span></th>
              <g:if test="${organizationInstance.groups}">
                <g:each in="${organizationInstance.groups}" var="g">
                  <td><span aria-labelledby="groups-label"><g:link controller="group" action="show" id="${g.id}">${g?.encodeAsHTML()}</g:link></span>
                </g:each>
              </g:if>
              <g:else>
                <td><span aria-labelledby="groups-label"><g:message code="label.none" /></span>
              </g:else>
            </tr>
          
            <tr>
              <th class="span4"><span id="subjectlimit-label"><strong><g:message code="label.subjectlimit" /></strong></span></th>
              <td><span aria-labelledby="subjectlimit-label"><g:fieldValue bean="${organizationInstance}" field="subjectLimit"/></span>
            </tr>
          
            <tr>
              <th class="span4"><span id="subjects-label"><strong><g:message code="label.subjects" /></strong></span></th>
              <g:if test="${organizationInstance.subjects}">
                <g:each in="${organizationInstance.subjects}" var="s">
                  <td><span aria-labelledby="subjects-label"><g:link controller="managedSubject" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></span>
                </g:each>
              </g:if>
              <g:else>
                <td><span aria-labelledby="subjects-label"><g:message code="label.none" /></span>
              </g:else>
            </tr>
          
            <tr>
              <th class="span4"><span id="undergoingworkflow-label"><strong><g:message code="label.undergoingworkflow" /></strong></span></th>
              <td><span aria-labelledby="undergoingworkflow-label"><g:formatBoolean boolean="${organizationInstance?.undergoingWorkflow}" /></span>
            </tr>
          
          </tbody>
        </table>
      </div>
    </div>

  </body>
</html>
