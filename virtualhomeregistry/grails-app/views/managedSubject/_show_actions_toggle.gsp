<g:if test="${managedSubjectInstance.login != null}">
  <a href="#" onclick="$(this).next('form').submit();">
    <g:if test="${managedSubjectInstance.active}">
      <g:message code="views.aaf.vhr.managedsubject.show.deactivate"/>
    </g:if>
    <g:else>
      <g:message code="views.aaf.vhr.managedsubject.show.activate"/>
    </g:else>
  </a>
  <g:form action="toggleActive" method="post">
    <g:hiddenField name="version" value="${managedSubjectInstance?.version}" />
    <g:hiddenField name="id" value="${managedSubjectInstance.id}" />
  </g:form>
</g:if>
