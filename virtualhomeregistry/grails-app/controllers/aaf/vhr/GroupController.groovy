package aaf.vhr

import org.springframework.dao.DataIntegrityViolationException
import org.apache.shiro.SecurityUtils

class GroupController {

  static defaultAction = "list"
  static allowedMethods = [save: "POST", update: "POST", delete: "DELETE"]

  def beforeInterceptor = [action: this.&validGroup, except: ['list', 'create', 'save']]

  def list() {
    log.info "Action: list, Subject: $subject"
    [groupInstanceList: Group.list(params), groupInstanceTotal: Group.count()]
  }

  def show(Long id) {
    log.info "Action: show, Subject: $subject"
    def groupInstance = Group.get(id)
    [groupInstance: groupInstance]
  }

  def create() {
    if(SecurityUtils.subject.isPermitted("app:manage:group:create")) {
      log.info "Action: create, Subject: $subject"
      [groupInstance: new Group(params)]
    }
    else {
      log.warn "Attempt to do administrative Group create by $subject was denied - not permitted by assigned permissions"
      response.sendError 403
    }
  }

  def save() {
    if(SecurityUtils.subject.isPermitted("app:manage:group:create")) {
      def groupInstance = new Group(params)
      if (!groupInstance.save()) {
        flash.type = 'error'
        flash.message = 'controllers.aaf.vhr.group.save.failed'
        render(view: "create", model: [groupInstance: groupInstance])
        return
      }

      log.info "Action: save, Subject: $subject, Object: groupInstance"
      flash.type = 'success'
      flash.message = 'controllers.aaf.vhr.group.save.success'
      redirect(action: "show", id: groupInstance.id)
    }
    else {
      log.warn "Attempt to do administrative Group save by $subject was denied - not permitted by assigned permissions"
      response.sendError 403
    }
  }

  def edit(Long id) {
    if(SecurityUtils.subject.isPermitted("app:manage:group:$id:edit")) {
      log.info "Action: edit, Subject: $subject, Object: groupInstance"
      def groupInstance = Group.get(id)
      [groupInstance: groupInstance]
    }
    else {
      log.warn "Attempt to do administrative Group edit by $subject was denied - not permitted by assigned permissions"
      response.sendError 403
    }
  }

  def update(Long id, Long version) {
    if(SecurityUtils.subject.isPermitted("app:manage:group:$id:edit")) {
      def groupInstance = Group.get(id)
      
      if (version == null) {
        flash.type = 'error'
        flash.message = 'controllers.aaf.vhr.group.update.noversion'
        render(view: "edit", model: [groupInstance: groupInstance])
        return
      }

      if (groupInstance.version > version) {
        groupInstance.errors.rejectValue("version", "controllers.aaf.vhr.group.update.optimistic.locking.failure")
        render(view: "edit", model: [groupInstance: groupInstance])
        return
      }

      groupInstance.properties = params

      if (!groupInstance.save()) {
        flash.type = 'error'
        flash.message = 'controllers.aaf.vhr.group.update.failed'
        render(view: "edit", model: [groupInstance: groupInstance])
        return
      }

      log.info "Action: update, Subject: $subject, Object: groupInstance"
      flash.type = 'success'
      flash.message = 'controllers.aaf.vhr.group.update.success'
      redirect(action: "show", id: groupInstance.id)
    }
    else {
      log.warn "Attempt to do administrative Group update by $subject was denied - not permitted by assigned permissions"
      response.sendError 403
    }
  }

  def delete(Long id) {
    if(SecurityUtils.subject.isPermitted("app:manage:group:$id:delete")) {
      def groupInstance = Group.get(id)
      try {
        groupInstance.delete()

        log.info "Action: delete, Subject: $subject, Object: groupInstance"
        flash.type = 'success'
        flash.message = 'controllers.aaf.vhr.group.delete.success'
        redirect(action: "list")
      }
      catch (DataIntegrityViolationException e) {
        flash.type = 'error'
        flash.message = 'controllers.aaf.vhr.group.delete.failure'
        redirect(action: "show", id: id)
      }
    }
    else {
      log.warn "Attempt to do administrative Group delete by $subject was denied - not permitted by assigned permissions"
      response.sendError 403
    }
  }

  private validGroup() {
    if(!params.id) {
      log.warn "ID was not present"

      flash.type = 'info'
      flash.message = message(code: 'controllers.aaf.vhr.group.no.id')

      redirect action:'list'
      return false
    }

    def groupInstance = Group.get(params.id)
    if (!groupInstance) {
      log.warn "groupInstance was not a valid instance"

      flash.type = 'info'
      flash.message = 'controllers.aaf.vhr.group.notfound'

      redirect action:'list'
      return false
    }
  }
}
