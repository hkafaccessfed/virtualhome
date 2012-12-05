<g:if test="${role.subjects}">  
  <table class="table table-borderless table-sortable">
    <thead>
      <tr>
        <th><g:message code="label.id" /></th>
        <th><g:message code="label.name" /></th>
        <th><g:message code="label.email" /></th>
        <th><g:message code="label.sharedtoken" /></th>
        <th/>
      </tr>
    </thead>
    <tbody>
      <g:each in="${role.subjects.sort{it.id}}" var="subject">
        <tr>
          <td><g:fieldValue bean="${subject}" field="id"/></td>
          <td><g:fieldValue bean="${subject}" field="cn"/></td>
          <td><a href="mailto:${subject.email}"><g:fieldValue bean="${subject}" field="email"/></a></td>
          <td class="hidden-phone"><g:fieldValue bean="${subject}" field="sharedToken"/></td>
          <td class="pull-right">
            
            <g:form controller="manageAdministrators" action="remove" method="post">
              <g:link controller="subject" action="show" id="${subject.id}" class="btn btn-small"><g:message code="label.view" /></g:link>

              <g:hiddenField name="type" value="organization" />
              <g:hiddenField name="id" value="${instance?.id}" />
              <g:hiddenField name="subjectID" value="${subject.id}" />
              <a class="remove-administrative-member btn btn-small btn-danger"><g:message code="label.remove"/></a>
            </g:form>
          </td>
        </tr>
      </g:each>
    </tbody>
  </table>
</g:if>
<g:else>
  <p class="alert alert-info"><g:message code="templates.aaf.vhr.manageadministrators.role.members.none"/></p>
</g:else>

