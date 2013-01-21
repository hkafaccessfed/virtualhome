<g:if test="${managedSubjectInstance.login == null}">
  <li>
    <a href="#" onclick="$(this).next('form').submit();"><g:message code="views.aaf.vhr.managedsubject.show.resend.welcome"/></a>
    <g:form action="resend" method="post">
      <g:hiddenField name="id" value="${managedSubjectInstance.id}" />
    </g:form>
  </li>
  <li class="divider"></li>
</g:if>
