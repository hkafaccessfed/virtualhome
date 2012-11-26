
<html>
  <head>
    <meta name="layout" content="internal" />
  </head>

  <body>

    <ul class="breadcrumb">
      <li><g:link controller="dashboard"><g:message code="branding.application.name"/></g:link> <span class="divider">/</span></li>
      <li><g:link action="list"><g:message code="branding.nav.breadcrumb.group"/></g:link> <span class="divider">/</span></li>
      <li><g:message code="branding.nav.breadcrumb.group.show"/></li>
    </ul>

    <g:render template="/templates/flash" plugin="aafApplicationBase"/>
    <g:render template="/templates/errors_bean" model="['bean':groupInstance]" plugin="aafApplicationBase"/>

    <h2><g:message code="views.aaf.vhr.group.show.heading" args="[]"/></h2>

    <ul class="nav nav-tabs">
      <li class="active"><a href="#tab-overview" data-toggle="tab"><g:message code="label.overview" /></a></li>
      <li class="dropdown pull-right">
        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
          <g:message code="label.actions" />
          <b class="caret"></b>
        </a>
        <ul class="dropdown-menu">
          <li>
            <g:link action="edit" id="${groupInstance.id}"><g:message code="label.edit"/></g:link>
            <a href="#" class="delete-ensure" data-confirm="${message(code:'views.aaf.vhr.group.confirm.remove')}"><g:message code="label.delete"/></a>
            <g:form action="delete" method="delete">
              <g:hiddenField name="id" value="${groupInstance.id}" />
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
              <td><span aria-labelledby="name-label"><g:fieldValue bean="${groupInstance}" field="name"/></span>
            </tr>
          
            <tr>
              <th class="span4"><span id="description-label"><strong><g:message code="label.description" /></strong></span></th>
              <td><span aria-labelledby="description-label"><g:fieldValue bean="${groupInstance}" field="description"/></span>
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
          
            <tr>
              <th class="span4"><span id="organization-label"><strong><g:message code="label.organization" /></strong></span></th>
              <td><span aria-labelledby="organization-label"><g:link controller="organization" action="show" id="${groupInstance?.organization?.id}">${groupInstance?.organization?.encodeAsHTML()}</g:link></span>
            </tr>
          
            <tr>
              <th class="span4"><span id="subjects-label"><strong><g:message code="label.subjects" /></strong></span></th>
              <g:if test="${groupInstance.subjects}">
                <g:each in="${groupInstance.subjects}" var="s">
                  <td><span aria-labelledby="subjects-label"><g:link controller="managedSubject" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></span>
                </g:each>
              </g:if>
              <g:else>
                <td><span aria-labelledby="subjects-label"><g:message code="label.none" /></span>
              </g:else>
            </tr>
          
          </tbody>
        </table>
      </div>
    </div>

  </body>
</html>
