<!doctype html>
<html>
  <head>
    <meta name="layout" content="internal" />
    <r:require modules="equalizecols" />
  </head>
  <body>

    <div class="row dashboardrow">
      <div class="span6">
        <div class="well well-small administering">
          <h2><g:message code="views.aaf.vhr.dashboard.myorganizations"/></h2>
          <g:if test="${organizationInstanceList}">
            <ol>
            <g:each in="${organizationInstanceList.sort{it.displayName}}" var="org">
              <li><g:link controller="organization" action="show" id="${org.id}"><g:fieldValue bean="${org}" field="displayName"/></g:link></li>
            </g:each>
            </ol>
          </g:if>
          <g:else>
            <p><g:message code="views.aaf.vhr.dashboard.noorganizations"/></p>
          </g:else>
          <br>
          <div class="view-all"><g:link controller="organization" action="list" class="btn btn-small btn-info"><g:message code="views.aaf.vhr.dashboard.organizations.view"/></g:link></div>
        </div>
      </div>

      <div class="span6">
        <div class="well well-small administering">
          <h2><g:message code="views.aaf.vhr.dashboard.mygroups"/></h2>
          <g:if test="${groupInstanceList}">
            <ol>
            <g:each in="${groupInstanceList.sort{it.organization.displayName}}" var="group">
              <li><g:link controller="group" action="show" id="${group.id}"><g:fieldValue bean="${group}" field="name"/></g:link> (<g:fieldValue bean="${group.organization}" field="displayName"/>)</li>
            </g:each>
            </ol>
          </g:if>
          <g:else>
            <p><g:message code="views.aaf.vhr.dashboard.nogroups"/></p>
          </g:else>
          <br>
          <div class="view-all"><g:link controller="group" action="list" class="btn btn-small btn-info"><g:message code="views.aaf.vhr.dashboard.groups.view"/></g:link></div>
        </div>
      </div>
    </div>

    <hr>

    <h2><g:message code="views.aaf.vhr.dashboard.statistics.heading"/></h2>
    <div class="row">
      <div class="span4">
        <div class="well well-small statistic">
          <h3><g:message code="views.aaf.vhr.dashboard.total.organizations"/></h3>
          <span class="lead">${statistics.organizations}</span>
        </div>
      </div>
      <div class="span4">
        <div class="well well-small statistic">
          <h3><g:message code="views.aaf.vhr.dashboard.total.groups"/></h3>
          <span class="lead">${statistics.groups}</span>
        </div>
      </div>
      <div class="span4">
        <div class="well well-small statistic">
          <h3><g:message code="views.aaf.vhr.dashboard.total.managedsubjects"/></h3>
          <span class="lead">${statistics.managedSubjects}</span>
        </div>
      </div>
    </div>

    <r:script>
      $('.administering').equalizeCols();
      $('.statistic').equalizeCols();
    </r:script>

  </body>
</html>
