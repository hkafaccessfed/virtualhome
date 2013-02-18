<ul class="dropdown-menu">

  <aaf:hasPermission target="app:administrator">
    <li>
      <g:link action="create" controller="managedSubject" params='['group.id':"${groupInstance.id}"]'><g:message code="label.createmanagedsubject"/></g:link>
    </li>
    <li>
      <g:link action="create" controller="managedSubject" params='['group.id':"${groupInstance.id}"]'><g:message code="label.createmultiplemanagedsubject"/></g:link>
    </li>

    <li class="divider"></li>
    
    <li>
      <g:render template="show_actions_admin"/>
    </li>

    <li class="divider"></li>

    <li>
      <g:link action="edit" id="${groupInstance.id}"><g:message code="label.editgroup"/></g:link>
    </li>
    <li>
      <g:render template="show_actions_toggle"/>
    </li>
    <li>
      <a href="#" onclick="$(this).next('form').submit();">
      <g:if test="${groupInstance.archived}">
        <g:message code="views.aaf.vhr.group.show.unarchive"/>
      </g:if>
      <g:else>
        <g:message code="views.aaf.vhr.group.show.archive"/>
      </g:else>
      </a>
      <g:form action="toggleArchived" method="post">
        <g:hiddenField name="version" value="${groupInstance?.version}" />
        <g:hiddenField name="id" value="${groupInstance.id}" />
      </g:form>
    </li>
    <li>
      <a href="#" onclick="$(this).next('form').submit();">
      <g:if test="${groupInstance.blocked}">
        <g:message code="views.aaf.vhr.group.show.unblock"/>
      </g:if>
      <g:else>
        <g:message code="views.aaf.vhr.group.show.block"/>
      </g:else>
      </a>
      <g:form action="toggleBlocked" method="post">
        <g:hiddenField name="version" value="${groupInstance?.version}" />
        <g:hiddenField name="id" value="${groupInstance.id}" />
      </g:form>
    </li>
    <li>
      <a href="#" class="delete-ensure" data-confirm="${message(code:'views.aaf.vhr.group.confirm.remove')}"><g:message code="label.deletegroup"/></a>
      <g:form action="delete" method="delete">
        <g:hiddenField name="id" value="${groupInstance.id}" />
      </g:form>
    </li>
  </aaf:hasPermission>

  <aaf:lacksPermission target="aaf:administrator">
    <aaf:hasAnyPermission in='["app:manage:organization:${groupInstance.organization.id}:group:${groupInstance.id}:managedsubject:create","app:manage:organization:${groupInstance.organization.id}:group:${groupInstance.id}:edit", "app:manage:organization:${groupInstance.organization.id}:group:${groupInstance.id}:manage:administrators"]'>
      <g:if test="${groupInstance.functioning()}">

        <aaf:hasPermission target="app:manage:organization:${groupInstance.organization.id}:group:${groupInstance.id}:create">
          <li>
            <g:link action="create" controller="managedSubject" params='['group.id':"${groupInstance.id}"]'><g:message code="label.createmanagedsubject"/></g:link>
          </li>
          <li>
            <g:link action="create" controller="managedSubject" params='['group.id':"${groupInstance.id}"]'><g:message code="label.createmultiplemanagedsubject"/></g:link>
          </li>
        </aaf:hasPermission>

        <li class="divider"></li>

        <aaf:hasPermission target="app:manage:organization:${groupInstance.organization.id}:group:${groupInstance.id}:manage:administrators">
          <li>
            <g:render template="show_actions_admin"/>
          </li>
        </aaf:hasPermission>

        <li class="divider"></li>
        
        <aaf:hasPermission target="app:manage:organization:${groupInstance.organization.id}:group:${groupInstance.id}:edit">
          <li>
            <g:link action="edit" id="${groupInstance.id}"><g:message code="label.editgroup"/></g:link>
          </li>
        </aaf:hasPermission>
      </g:if>

      <aaf:hasPermission target="app:manage:organization:${groupInstance.organization.id}:group:${groupInstance.id}:edit">
        <li>
          <g:render template="show_actions_toggle"/>
        </li>
      </aaf:hasPermission>

    </aaf:hasAnyPermission>
  </aaf:lacksPermission>

  <aaf:lacksAllPermissions in='["app:manage:organization:${groupInstance.organization.id}:group:${groupInstance.id}:managedsubject:create","app:manage:organization:${groupInstance.organization.id}:group:${groupInstance.id}:edit", "app:manage:organization:${groupInstance.organization.id}:group:${groupInstance.id}:manage:administrators", "app:administrator"]'>
    <li><span class="label label-important" style="margin:6px;"><g:message code="label.noactions"/></span>
  </aaf:lacksAllPermissions>

</ul>
