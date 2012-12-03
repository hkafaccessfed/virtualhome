package aaf.vhr

import org.springframework.dao.DataIntegrityViolationException
import org.apache.shiro.SecurityUtils

class ManagedSubjectController {

  static defaultAction = "list"
  static allowedMethods = [save: "POST", update: "POST", delete: "DELETE", resend:"POST"]

  def beforeInterceptor = [action: this.&validManagedSubject, except: ['list', 'create', 'save']]

  def managedSubjectService
  def sharedTokenService

  def list() {
    log.info "Action: list, Subject: $subject"
    [managedSubjectInstanceList: ManagedSubject.list(params), managedSubjectInstanceTotal: ManagedSubject.count()]
  }

  def show(Long id) {
    log.info "Action: show, Subject: $subject"
    def managedSubjectInstance = ManagedSubject.get(id)
    [managedSubjectInstance: managedSubjectInstance]
  }

  def create() {
    if(validGroup()) {
      if(SecurityUtils.subject.isPermitted("app:manage:group:${params.group.id}:managedsubject:create")) {
        log.info "Action: create, Subject: $subject"
        def group = Group.get(params.group.id)
        def managedSubjectInstance = new ManagedSubject(group:group, organization:group.organization)
        [managedSubjectInstance: managedSubjectInstance]
      }
      else {
        log.warn "Attempt to do administrative ManagedSubject create by $subject was denied - not permitted by assigned permissions"
        response.sendError 403
      }
    }
  }

  def save() {
    if(validGroup()) {
      if(SecurityUtils.subject.isPermitted("app:manage:group:${params.group.id}:managedsubject:create")) {
        def group = Group.get(params.group.id)
        def managedSubjectInstance = new ManagedSubject()
        bindData(managedSubjectInstance, params, [include: ['cn', 'email', 'eduPersonAssurance', 'eduPersonAffiliation']])
        managedSubjectInstance.displayName = managedSubjectInstance.cn
        managedSubjectInstance.group = group
        managedSubjectInstance.organization = group.organization
        sharedTokenService.generate(managedSubjectInstance)

        if (!managedSubjectInstance.validate()) {
          flash.type = 'error'
          flash.message = 'controllers.aaf.vhr.managedsubject.validate.failed'
          render(view: "create", model: [managedSubjectInstance: managedSubjectInstance])
          return
        }

        managedSubjectService.register(managedSubjectInstance)

        log.info "Action: save, Subject: $subject, Object: $managedSubjectInstance"
        flash.type = 'success'
        flash.message = 'controllers.aaf.vhr.managedsubject.save.success'
        redirect(action: "show", id: managedSubjectInstance.id)
      }
      else {
        log.warn "Attempt to do administrative ManagedSubject save by $subject was denied - not permitted by assigned permissions"
        response.sendError 403
      }
    }
  }

  def edit(Long id) {
    def managedSubjectInstance = ManagedSubject.get(id)
    if(SecurityUtils.subject.isPermitted("app:manage:group:${managedSubjectInstance.group.id}:managedsubject:edit")) {
      log.info "Action: edit, Subject: $subject, Object: managedSubjectInstance"

      [managedSubjectInstance: managedSubjectInstance]
    }
    else {
      log.warn "Attempt to do administrative ManagedSubject edit by $subject was denied - not permitted by assigned permissions"
      response.sendError 403
    }
  }

  def update(Long id, Long version) {
    def managedSubjectInstance = ManagedSubject.get(id)
    if(SecurityUtils.subject.isPermitted("app:manage:group:${managedSubjectInstance.group.id}:managedsubject:edit")) {
      if (version == null) {
        flash.type = 'error'
        flash.message = 'controllers.aaf.vhr.managedsubject.update.noversion'
        render(view: "edit", model: [managedSubjectInstance: managedSubjectInstance])
        return
      }

      if (managedSubjectInstance.version > version) {
        managedSubjectInstance.errors.rejectValue("version", "controllers.aaf.vhr.managedsubject.update.optimistic.locking.failure")
        render(view: "edit", model: [managedSubjectInstance: managedSubjectInstance])
        return
      }

      bindData(managedSubjectInstance, params, [include: ['cn', 'email', 'eduPersonAssurance', 'eduPersonAffiliation', 'displayName', 
                                                          'givenName', 'surname', 'mobileNumber', 'telephoneNumber', 'postalAddress', 
                                                          'organizationalUnit']])

      if (!managedSubjectInstance.save()) {
        flash.type = 'error'
        flash.message = 'controllers.aaf.vhr.managedsubject.update.failed'
        render(view: "edit", model: [managedSubjectInstance: managedSubjectInstance])
        return
      }

      log.info "Action: update, Subject: $subject, Object: $managedSubjectInstance"
      flash.type = 'success'
      flash.message = 'controllers.aaf.vhr.managedsubject.update.success'
      redirect(action: "show", id: managedSubjectInstance.id)
    }
    else {
      log.warn "Attempt to do administrative ManagedSubject update by $subject was denied - not permitted by assigned permissions"
      response.sendError 403
    }
  }

  def delete(Long id) {
    def managedSubjectInstance = ManagedSubject.get(id)
    if(SecurityUtils.subject.isPermitted("app:manage:group:${managedSubjectInstance.group.id}:managedsubject:delete")) {
      try {
        managedSubjectInstance.delete()

        log.info "Action: delete, Subject: $subject, Object: $managedSubjectInstance"
        flash.type = 'success'
        flash.message = 'controllers.aaf.vhr.managedsubject.delete.success'
        redirect(action: "list")
      }
      catch (DataIntegrityViolationException e) {
        flash.type = 'error'
        flash.message = 'controllers.aaf.vhr.managedsubject.delete.failure'
        redirect(action: "show", id: id)
      }
    }
    else {
      log.warn "Attempt to do administrative ManagedSubject delete by $subject was denied - not permitted by assigned permissions"
      response.sendError 403
    }
  }

  def resend(Long id) {
    def managedSubjectInstance = ManagedSubject.get(id)
    if(SecurityUtils.subject.isPermitted("app:manage:group:${managedSubjectInstance.group.id}:managedsubject:edit")) {
      managedSubjectService.sendConfirmation(managedSubjectInstance)

      log.info "Action: resend, Subject: $subject, Object: $managedSubjectInstance"
      flash.type = 'success'
      flash.message = 'controllers.aaf.vhr.managedsubject.resend.success'
      redirect(action: "show", id: managedSubjectInstance.id)
    }
    else {
      log.warn "Attempt to do administrative ManagedSubject resend by $subject was denied - not permitted by assigned permissions"
      response.sendError 403
    }
  }

  def toggleLock(Long id, Long version) {
    def managedSubjectInstance = ManagedSubject.get(id)
    if(SecurityUtils.subject.isPermitted("app:administration")) {
      if (version == null) {
        flash.type = 'error'
        flash.message = 'controllers.aaf.vhr.managedsubject.togglelock.noversion'
        redirect(action: "show", id: managedSubjectInstance.id)
        return
      }

      if (managedSubjectInstance.version > version) {
        managedSubjectInstance.errors.rejectValue("version", "controllers.aaf.vhr.managedsubject.togglelock.optimistic.locking.failure")
        redirect(action: "show", id: managedSubjectInstance.id)
        return
      }

      managedSubjectInstance.locked = !managedSubjectInstance.locked

      if (!managedSubjectInstance.save()) {
        flash.type = 'error'
        flash.message = 'controllers.aaf.vhr.managedsubject.togglelock.failed'
        redirect(action: "show", id: managedSubjectInstance.id)
        return
      }

      log.info "Action: toggleLock, Subject: $subject, Object: $managedSubjectInstance"
      flash.type = 'success'
      flash.message = 'controllers.aaf.vhr.managedsubject.togglelock.success'
      redirect(action: "show", id: managedSubjectInstance.id)
    }
    else {
      log.warn "Attempt to do administrative ManagedSubject togglelock by $subject was denied - not permitted by assigned permissions"
      response.sendError 403
    }
  }

  def toggleActive(Long id, Long version) {
    def managedSubjectInstance = ManagedSubject.get(id)
    if(SecurityUtils.subject.isPermitted("app:manage:group:${managedSubjectInstance.group.id}:managedsubject:edit")) {
      if (version == null) {
        flash.type = 'error'
        flash.message = 'controllers.aaf.vhr.managedsubject.toggleactive.noversion'
        redirect(action: "show", id: managedSubjectInstance.id)
        return
      }

      if (managedSubjectInstance.version > version) {
        managedSubjectInstance.errors.rejectValue("version", "controllers.aaf.vhr.managedsubject.toggleactive.optimistic.locking.failure")
        redirect(action: "show", id: managedSubjectInstance.id)
        return
      }

      managedSubjectInstance.active = !managedSubjectInstance.active

      if (!managedSubjectInstance.save()) {
        flash.type = 'error'
        flash.message = 'controllers.aaf.vhr.managedsubject.toggleactive.failed'
        redirect(action: "show", id: managedSubjectInstance.id)
        return
      }

      log.info "Action: toggleActive, Subject: $subject, Object: $managedSubjectInstance"
      flash.type = 'success'
      flash.message = 'controllers.aaf.vhr.managedsubject.toggleactive.success'
      redirect(action: "show", id: managedSubjectInstance.id)
    }
    else {
      log.warn "Attempt to do administrative ManagedSubject toggleactive by $subject was denied - not permitted by assigned permissions"
      response.sendError 403
    }
  }

  private validGroup() {
    if(!params.group?.id) {
      log.warn "Group ID was not present"

      flash.type = 'info'
      flash.message = message(code: 'controllers.aaf.vhr.managedsubject.group.no.id')

      redirect action:'list'
      return false
    }

    def groupInstance = Group.get(params.group.id)
    if (!groupInstance) {
      log.warn "groupInstance was not a valid instance"

      flash.type = 'info'
      flash.message = 'controllers.aaf.vhr.managedsubject.group.notfound'

      redirect action:'list'
      return false
    }

    true
  }

  private validManagedSubject() {
    if(!params.id) {
      log.warn "ID was not present"

      flash.type = 'info'
      flash.message = message(code: 'controllers.aaf.vhr.managedsubject.no.id')

      redirect action:'list'
      return false
    }

    def managedSubjectInstance = ManagedSubject.get(params.id)
    if (!managedSubjectInstance) {
      log.warn "managedSubjectInstance was not a valid instance"

      flash.type = 'info'
      flash.message = 'controllers.aaf.vhr.managedsubject.notfound'

      redirect action:'list'
      return false
    }
  }
}
