<ul class="dropdown-menu">

  <aaf:hasPermission target="app:administrator">
    <li>
      <g:link action="createaccount" params='['id':"${organizationInstance.id}"]'><g:message code="label.createmanagedsubject"/></g:link>
    </li>
    <li>
      <g:link action="create" controller="group" params='['organization.id':"${organizationInstance.id}"]'><g:message code="label.creategroup"/></g:link>
    </li>

    <li class="divider"></li>
    
    <li>
      <g:render template="show_actions_admin"/>
    </li>
    <li class="divider"></li>
    <li>
      <g:link action="edit" id="${organizationInstance.id}"><g:message code="label.editorganization"/></g:link>
    </li>
    <li>
      <g:render template="show_actions_toggle"/>
    </li>

    <li class="divider"></li>
    
    <li>
      <a href="#" onclick="$(this).next('form').submit();">
        <g:if test="${organizationInstance.archived}">
          <g:message code="views.aaf.vhr.organization.show.unarchive"/>
        </g:if>
        <g:else>
          <g:message code="views.aaf.vhr.organization.show.archive"/>
        </g:else>
      </a>
      <g:form action="toggleArchive" method="post">
        <g:hiddenField name="version" value="${organizationInstance?.version}" />
        <g:hiddenField name="id" value="${organizationInstance.id}" />
      </g:form>
    </li>

    <li>
      <a href="#" onclick="$(this).next('form').submit();">
        <g:if test="${organizationInstance.blocked}">
          <g:message code="views.aaf.vhr.organization.show.unblock"/>
        </g:if>
        <g:else>
          <g:message code="views.aaf.vhr.organization.show.block"/>
        </g:else>
      </a>
      <g:form action="toggleBlocked" method="post">
        <g:hiddenField name="version" value="${organizationInstance?.version}" />
        <g:hiddenField name="id" value="${organizationInstance.id}" />
      </g:form>
    </li>
    </li>
    <li>
      <a href="#" class="delete-ensure" data-confirm="${message(code:'views.aaf.vhr.organization.confirm.remove')}"><g:message code="label.deleteorganization"/></a>
      <g:form action="delete" method="delete">
        <g:hiddenField name="id" value="${organizationInstance.id}" />
      </g:form>
    </li>
  </aaf:hasPermission>

  <aaf:lacksPermission target="aaf:administrator">
    <aaf:hasAnyPermission in='["app:manage:organization:${organizationInstance.id}:groups:create", "app:manage:organization:${organizationInstance.id}:edit", "app:manage:organization:${organizationInstance.id}:manage:administrators"]'>
      <g:if test="${organizationInstance.functioning()}">
        <aaf:hasPermission target="app:manage:organization:${organizationInstance.id}:edit">
          <li>
            <g:link action="createaccount" params='['id':"${organizationInstance.id}"]'><g:message code="label.createmanagedsubject"/></g:link>
          </li>
        </aaf:hasPermission>
        <aaf:hasPermission target="app:manage:organization:${organizationInstance.id}:groups:create">
          <li>
            <g:link action="create" controller="group" params='['organization.id':"${organizationInstance.id}"]'><g:message code="label.creategroup"/></g:link>
          </li>
        </aaf:hasPermission>
        <aaf:hasPermission target="app:manage:organization:${organizationInstance.id}:manage:administrators">
          <li class="divider"></li>
          <li>
            <g:render template="show_actions_admin"/>
          </li>
        </aaf:hasPermission> 

        <li class="divider"></li>
      
        <aaf:hasPermission target="app:manage:organization:${organizationInstance.id}:edit">
          <li>
            <g:link action="edit" id="${organizationInstance.id}"><g:message code="label.editorganization"/></g:link>
          </li>
        </aaf:hasPermission>
      </g:if>
      <aaf:hasPermission target="app:manage:organization:${organizationInstance.id}:edit">
        <li>
          <g:render template="show_actions_toggle"/>
        </li>
      </aaf:hasPermission>
    </aaf:hasAnyPermission>
  </aaf:lacksPermission>

  <aaf:lacksAllPermissions in='["app:manage:organization:${organizationInstance.id}:groups:create", "app:manage:organization:${organizationInstance.id}:edit", "app:administrator"]'>
    <li><span class="label label-important" style="margin:6px;"><g:message code="label.noactions"/></span>
  </aaf:lacksAllPermissions>

</ul>
