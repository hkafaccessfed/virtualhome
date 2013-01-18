<ul class="dropdown-menu">
  <aaf:hasPermission target="app:administrator">
    <g:render template="show_breadcrumb_resend"/>

    <li>
      <g:link action="edit" id="${managedSubjectInstance.id}"><g:message code="label.editmanagedsubject"/></g:link>
    </li>

    <li>
       <g:render template="show_breadcrumb_toggle"/>
    </li>

    <li class="divider"></li>

    <li>
      <a href="#" onclick="$(this).next('form').submit();">
        <g:if test="${managedSubjectInstance.locked}">
          <g:message code="views.aaf.vhr.managedsubject.show.admin.unlock"/>
        </g:if>
        <g:else>
          <g:message code="views.aaf.vhr.managedsubject.show.admin.lock"/>
        </g:else>
      </a>
      <g:form action="toggleLock" method="post">
        <g:hiddenField name="version" value="${managedSubjectInstance?.version}" />
        <g:hiddenField name="id" value="${managedSubjectInstance.id}" />
      </g:form>
    </li>

    <li class="divider"></li>

    <li>
      <a href="#" class="delete-ensure" data-confirm="${message(code:'views.aaf.vhr.managedsubject.confirm.remove')}"><g:message code="label.deletemanagedsubject"/></a>
      <g:form action="delete" method="delete">
        <g:hiddenField name="id" value="${managedSubjectInstance.id}" />
      </g:form>
    </li>
  </aaf:hasPermission>

  <aaf:lacksPermission target="aaf:administrator">
    <aaf:hasPermission target="app:manage:organization:${managedSubjectInstance.organization.id}:group:${managedSubjectInstance.group.id}:managedsubject:edit">
      <g:render template="show_breadcrumb_resend"/>
      <g:if test="${managedSubjectInstance.functioning()}">
        <li class="divider"></li>
        <li>
          <g:link action="edit" id="${managedSubjectInstance.id}"><g:message code="label.editmanagedsubject"/></g:link>
        </li>
      </g:if>
      <g:render template="show_breadcrumb_toggle"/>
    </aaf:hasPermission>
  </aaf:lacksPermission>

  <aaf:lacksAllPermissions in='["app:manage:organization:${managedSubjectInstance.organization.id}:group:${managedSubjectInstance.group.id}:managedsubject:edit", "app:administrator"]'>
    <li><span class="label label-important" style="margin:6px;"><g:message code="label.noactions"/></span>
  </aaf:lacksAllPermissions>
</ul>
