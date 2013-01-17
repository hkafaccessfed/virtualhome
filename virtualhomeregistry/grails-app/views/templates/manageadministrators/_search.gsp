<legend><g:message code="label.addmembers"/></legend>
<table class="table table-borderless table-sortable">
  <thead>
    <tr>
      <th><g:message code="label.name" /></th>
      <th><g:message code="label.sharedtoken" /></th>
      <th/>
    </tr>
  </thead>
  <tbody>
    <g:each in="${subjects}" var="subject">
      <tr>
        <td><g:fieldValue bean="${subject}" field="cn"/></td>
        <td><g:fieldValue bean="${subject}" field="sharedToken"/></td>
        <td class="pull-right">
        
          <g:form controller="manageAdministrators" action="add" method="post">
            <a href="#" class="btn btn-small ajax-modal" data-load="${createLink(controller:'subject', action:'showpublic', id:subject.id, absolute:true)}" ><g:message code="label.quickview" /></a>
            <g:link controller="subject" action="show" id="${subject.id}" class="btn btn-small"><g:message code="label.view" /></g:link>

            <g:hiddenField name="type" value="${type}" />
            <g:hiddenField name="id" value="${instance?.id}" />
            <g:hiddenField name="subjectID" value="${subject?.id}" />
            <a class="add-administrative-member btn btn-small btn-success"><g:message code="label.add"/></a>
          </g:form>
        </td>
    </g:each>
  </tbody>
</table>
