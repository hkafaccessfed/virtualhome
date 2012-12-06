<g:if test="${role.subjects}">  
  <table class="table table-borderless table-sortable">
    <thead>
      <tr>
        <th><g:message code="label.name" /></th>
        <th><g:message code="label.email" /></th>
      </tr>
    </thead>
    <tbody>
      <g:each in="${role.subjects.sort{it.id}}" var="subject">
        <tr>
          <td><g:fieldValue bean="${subject}" field="cn"/></td>
          <td><a href="mailto:${subject.email}"><g:fieldValue bean="${subject}" field="email"/></a></td>
        </tr>
      </g:each>
    </tbody>
  </table>
</g:if>
<g:else>
  <p class="alert alert-info"><g:message code="templates.aaf.vhr.manageadministrators.role.members.none"/></p>
</g:else>
