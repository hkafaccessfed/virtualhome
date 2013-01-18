<a class="show-add-administrative-members"><g:message code="label.addadministrator"/></a>
<g:form controller="manageAdministrators" action="search" method="post">
  <g:hiddenField name="id" value="${groupInstance.id}" />
  <g:hiddenField name="type" value="group" />
</g:form>
