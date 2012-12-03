
<html>
  <head>
    <meta name="layout" content="internal" />
  </head>

  <body>

    <ul class="breadcrumb">
      <li><g:link controller="dashboard"><g:message code="branding.application.name"/></g:link> <span class="divider">/</span></li>
      <li><g:link action="list" controller="organization"><g:message code="branding.nav.breadcrumb.organization"/></g:link> <span class="divider">/</span></li>
      <li><g:link action="show" controller="organization" id="${managedSubjectInstance.group.organization.id}"><g:fieldValue bean="${managedSubjectInstance.group.organization}" field="displayName"/></g:link> <span class="divider">/</span></li>
      <li><g:link controller="group" action="list"><g:message code="branding.nav.breadcrumb.group"/></g:link> <span class="divider">/</span></li>
      <li><g:link controller="group" action="show" id="${managedSubjectInstance.group.id}"> <g:fieldValue bean="${managedSubjectInstance.group}" field="name"/></g:link> <span class="divider">/</span></li>
      <li><g:link action="list"><g:message code="branding.nav.breadcrumb.managedsubject"/></g:link> <span class="divider">/</span></li>
      <li class="active"><g:fieldValue bean="${managedSubjectInstance}" field="cn"/></li>
    </ul>

    <g:render template="/templates/flash" plugin="aafApplicationBase"/>
    <g:render template="/templates/errors_bean" model="['bean':managedSubjectInstance]" plugin="aafApplicationBase"/>

    <h2><g:message code="views.aaf.vhr.managedsubject.show.heading" args="${[managedSubjectInstance.cn]}"/></h2>

    <g:if test="${managedSubjectInstance.locked}">
      <div class="alert alert-block alert-warning">
        <h4><g:message code="views.aaf.vhr.managedsubject.show.locked.heading"/></h4>
        <p><g:message code="views.aaf.vhr.managedsubject.show.locked.reason"/></p>
        <p><g:message code="views.aaf.vhr.managedsubject.show.locked.correct"/></p>
      </div>
    </g:if>
    <g:if test="${managedSubjectInstance.login == null}">
      <div class="alert alert-block alert-info">
        <h4><g:message code="views.aaf.vhr.managedsubject.show.finalized.heading"/></h4>
        <p><g:message code="views.aaf.vhr.managedsubject.show.finalized.reason"/></p>
        <p><g:message code="views.aaf.vhr.managedsubject.show.finalized.correct"/></p>
      </div>
    </g:if>
    <g:if test="${!(managedSubjectInstance.functioning()) && managedSubjectInstance.login != null && !managedSubjectInstance.locked}">
      <div class="alert alert-block alert-error">
        <h4><g:message code="views.aaf.vhr.managedsubject.show.functioning.heading"/></h4>
        <p><g:message code="views.aaf.vhr.managedsubject.show.functioning.reason"/></p>
        <p><g:message code="views.aaf.vhr.managedsubject.show.unable.to.login"/></p>
      </div>
    </g:if>

    <ul class="nav nav-tabs">
      <li class="active"><a href="#tab-overview" data-toggle="tab"><g:message code="label.overview" /></a></li>

      <aaf:hasAnyPermission in='["app:manage:group:${managedSubjectInstance.group.id}:managedsubject:edit","app:manage:group:${managedSubjectInstance.group.id}:managedsubject:delete"]'>
      <li class="dropdown pull-right">
        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
          <g:message code="label.actions" />
          <b class="caret"></b>
        </a>
        <ul class="dropdown-menu">
          <aaf:hasPermission target="app:manage:group:${managedSubjectInstance.group.id}:managedsubject:edit">
            <g:if test="${managedSubjectInstance.login == null}">
              <g:form action="resend" method="post">
                <g:hiddenField name="id" value="${managedSubjectInstance.id}" />
                <a href="#" onclick="$(this).parents('form').submit();"><g:message code="views.aaf.vhr.managedsubject.show.resend.welcome"/></a>
              </g:form>

              <li class="divider"></li>
            </g:if>

            <li>
              <g:form action="toggleActive" method="post">
                <g:hiddenField name="version" value="${managedSubjectInstance?.version}" />
                <g:hiddenField name="id" value="${managedSubjectInstance.id}" />
                <a href="#" onclick="$(this).parents('form').submit();">
                  <g:if test="${managedSubjectInstance.active}">
                    <g:message code="views.aaf.vhr.managedsubject.show.deactivate"/>
                  </g:if>
                  <g:else>
                    <g:message code="views.aaf.vhr.managedsubject.show.activate"/>
                  </g:else>
                </a>
              </g:form>
            </li>

            <li>
              <g:link action="edit" id="${managedSubjectInstance.id}"><g:message code="label.edit"/></g:link>
            </li>
          </aaf:hasPermission>

          <aaf:hasPermission target="app:manage:group:${managedSubjectInstance.group.id}:managedsubject:delete">
            <li>
              <a href="#" class="delete-ensure" data-confirm="${message(code:'views.aaf.vhr.managedsubject.confirm.remove')}"><g:message code="label.delete"/></a>
              <g:form action="delete" method="delete">
                <g:hiddenField name="id" value="${managedSubjectInstance.id}" />
              </g:form>
            </li>
          </aaf:hasPermission>

          <aaf:hasPermission target="app:administrator">
            <li class="divider"></li>
            <li>
              <g:form action="toggleLock" method="post">
                <g:hiddenField name="version" value="${managedSubjectInstance?.version}" />
                <g:hiddenField name="id" value="${managedSubjectInstance.id}" />
                <a href="#" onclick="$(this).parents('form').submit();">
                  <g:if test="${managedSubjectInstance.locked}">
                    <g:message code="views.aaf.vhr.managedsubject.show.admin.unlock"/>
                  </g:if>
                  <g:else>
                    <g:message code="views.aaf.vhr.managedsubject.show.admin.lock"/>
                  </g:else>
                </a>
              </g:form>
            </li>
          </aaf:hasPermission>
        </ul>
      </li>
      </aaf:hasAnyPermission>
    </ul>

    <div class="tab-content">
      <div id="tab-overview" class="tab-pane active">
      <div class="row">
        <div class="span5">
          <table class="table table-borderless">
          <tbody>

            <tr><td colspan="2"><h4><g:message code="label.coreattributes"/></h4></tr></td>
            
            <tr>
              <th class="span4"><span id="cn-label"><strong><g:message code="label.cn" /></strong></span></th>
              <td><span aria-labelledby="cn-label"><g:fieldValue bean="${managedSubjectInstance}" field="cn"/></span>
            </tr>
          
            <tr>
              <th class="span4"><span id="email-label"><strong><g:message code="label.email" /></strong></span></th>
              <td><span aria-labelledby="email-label"><g:fieldValue bean="${managedSubjectInstance}" field="email"/></span>
            </tr>

            <tr>
              <th class="span4"><span id="sharedtoken-label"><strong><g:message code="label.sharedtoken" /></strong></span></th>
              <td><span aria-labelledby="sharedtoken-label"><g:fieldValue bean="${managedSubjectInstance}" field="sharedToken"/></span>
            </tr>
          
            <tr>
              <th class="span4"><span id="edupersonassurance-label"><strong><g:message code="label.edupersonassurance" /></strong></span></th>
              <td><span aria-labelledby="edupersonassurance-label"><g:fieldValue bean="${managedSubjectInstance}" field="eduPersonAssurance"/></span>
            </tr>
          
            <tr>
              <th class="span4"><span id="edupersonaffiliation-label"><strong><g:message code="label.edupersonaffiliation" /></strong></span></th>
              <td><span aria-labelledby="edupersonaffiliation-label"><g:fieldValue bean="${managedSubjectInstance}" field="eduPersonAffiliation"/></span>
            </tr>

            <tr>
              <th class="span4"><span id="displayname-label"><strong><g:message code="label.displayname" /></strong></span></th>
              <td><span aria-labelledby="displayname-label"><g:fieldValue bean="${managedSubjectInstance}" field="displayName"/></span>
            </tr>
          </tbody>
          </table>
        </div>

        <div class="span5 offset2">
          <table class="table table-borderless">
          <tbody>
            <tr><td colspan="2"><h4><g:message code="label.optionalattributes"/></h4></tr></td>

            <tr>
              <th class="span4"><span id="givenname-label"><strong><g:message code="label.givenname" /></strong></span></th>
              <td><span aria-labelledby="givenname-label"><g:fieldValue bean="${managedSubjectInstance}" field="givenName"/></span>
            </tr>
          
            <tr>
              <th class="span4"><span id="surname-label"><strong><g:message code="label.surname" /></strong></span></th>
              <td><span aria-labelledby="surname-label"><g:fieldValue bean="${managedSubjectInstance}" field="surname"/></span>
            </tr>
          
            <tr>
              <th class="span4"><span id="telephonenumber-label"><strong><g:message code="label.telephonenumber" /></strong></span></th>
              <td><span aria-labelledby="telephonenumber-label"><g:fieldValue bean="${managedSubjectInstance}" field="telephoneNumber"/></span>
            </tr>

            <tr>
              <th class="span4"><span id="mobilenumber-label"><strong><g:message code="label.mobilenumber" /></strong></span></th>
              <td><span aria-labelledby="mobilenumber-label"><g:fieldValue bean="${managedSubjectInstance}" field="mobileNumber"/></span>
            </tr>
          
            <tr>
              <th class="span4"><span id="postaladdress-label"><strong><g:message code="label.postaladdress" /></strong></span></th>
              <td><span aria-labelledby="postaladdress-label"><g:fieldValue bean="${managedSubjectInstance}" field="postalAddress"/></span>
            </tr>
          
            <tr>
              <th class="span4"><span id="organizationalunit-label"><strong><g:message code="label.organizationalunit" /></strong></span></th>
              <td><span aria-labelledby="organizationalunit-label"><g:fieldValue bean="${managedSubjectInstance}" field="organizationalUnit"/></span>
            </tr>

          </tbody>
          </table>
        </div>
      </div>

      <hr>

        <div class="row">
          <div class="span12">

            <table class="table table-borderless">
            <tbody>

            <tr><td colspan="2"><h4><g:message code="label.internaldata"/></h4></tr></td>

            <tr>
              <th class="span4"><span id="internalid-label"><strong><g:message code="label.internalid" /></strong></span></th>
              <td><span aria-labelledby="internalid-label"><g:fieldValue bean="${managedSubjectInstance}" field="id" /></span>
            </tr>

            <tr>
              <th class="span4"><span id="login-label"><strong><g:message code="label.login" /></strong></span></th>
              <td><span aria-labelledby="login-label"><g:fieldValue bean="${managedSubjectInstance}" field="login"/></span>
            </tr>

            <tr>
              <th class="span4"><span id="active-label"><strong><g:message code="label.active" /></strong></span></th>
              <td><span aria-labelledby="active-label"><g:formatBoolean boolean="${managedSubjectInstance?.active}" /></span>
            </tr>

            <tr>
              <th class="span4"><span id="locked-label"><strong><g:message code="label.locked" /></strong></span></th>
              <td><span aria-labelledby="locked-label"><g:formatBoolean boolean="${managedSubjectInstance?.locked}" /></span>
            </tr>

            <tr>
              <th class="span4"><span id="organization-label"><strong><g:message code="label.organization" /></strong></span></th>
              <td><span aria-labelledby="organization-label"><g:link controller="organization" action="show" id="${managedSubjectInstance?.organization?.id}"><g:fieldValue bean="${managedSubjectInstance.organization}" field="displayName"/></g:link></span>
            </tr>
          
            <tr>
              <th class="span4"><span id="group-label"><strong><g:message code="label.group" /></strong></span></th>
              <td><span aria-labelledby="group-label"><g:link controller="group" action="show" id="${managedSubjectInstance?.group?.id}"><g:fieldValue bean="${managedSubjectInstance.group}" field="name"/></g:link></span>
            </tr>
          
          </tbody>
        </table>
      </div>
    </div>

  </body>
</html>
