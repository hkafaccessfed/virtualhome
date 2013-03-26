package aaf.vhr

import groovy.time.TimeCategory
import aaf.base.identity.SessionRecord

import aaf.base.identity.Role

class AccountController {

  static allowedMethods = [completepasswordchange: 'POST']

  final CURRENT_USER = "aaf.vhr.AccountController.CURRENT_USER"

  def cryptoService
  def passwordValidationService

  def index() { 
  }

  def logout() {
    session.invalidate()
    redirect controller:'dashboard', action:'welcome'
  }

  def show() {
    def managedSubjectInstance = ManagedSubject.get(session.getAttribute(CURRENT_USER))

    if(!managedSubjectInstance) {
      if(!params.login) {
        redirect action:'index'
        return
      }

      managedSubjectInstance = ManagedSubject.findWhere(login: params.login)

      if(!managedSubjectInstance || !managedSubjectInstance.hash) {
        log.error "No such ManagedSubject for $params.login or password is not populated"
        
        flash.type = 'error'
        flash.message = 'controllers.aaf.vhr.account.login.error'
        render view: 'index'

        return
      }

      if(!cryptoService.verifyPasswordHash(params.plainPassword, managedSubjectInstance)) {
        log.error "Password invalid for $managedSubjectInstance"
        
        flash.type = 'error'
        flash.message = 'controllers.aaf.vhr.account.login.password.error'
        render view: 'index'

        return
      }

      session.setAttribute(CURRENT_USER, managedSubjectInstance.id)
    }

    flash.clear()

    def groupRole = Role.findWhere(name:"group:${managedSubjectInstance.group.id}:administrators")
    def organizationRole = Role.findWhere(name:"organization:${managedSubjectInstance.organization.id}:administrators")

    [managedSubjectInstance:managedSubjectInstance, groupRole:groupRole, organizationRole:organizationRole]
  }

  def changepassword() {
    def managedSubjectInstance = ManagedSubject.get(session.getAttribute(CURRENT_USER))

    if(!managedSubjectInstance) {
      log.error "No ManagedSubject stored in session, requesting login before accessing password change"
      
      flash.type = 'info'
      flash.message = 'controllers.aaf.vhr.account.changepassword.requireslogin'
      redirect action: 'index'

      return
    }

    [managedSubjectInstance:managedSubjectInstance]
  }

  def completepasswordchange() {
    def managedSubjectInstance = ManagedSubject.get(session.getAttribute(CURRENT_USER))

    if(!managedSubjectInstance) {
      log.error "A valid session does not already exist to allow completepassworchange to function"
      
      response.sendError 403

      return
    }

    if(!cryptoService.verifyPasswordHash(params.currentPassword, managedSubjectInstance)) {
      log.error "Password invalid for $managedSubjectInstance"
      
      flash.type = 'error'
      flash.message = 'controllers.aaf.vhr.account.completepasswordchange.password.error'
      render view: 'changepassword', model: [managedSubjectInstance:managedSubjectInstance]

      return
    }

    managedSubjectInstance.plainPassword = params.plainPassword
    managedSubjectInstance.plainPasswordConfirmation = params.plainPasswordConfirmation

    def (validPassword, errors) = passwordValidationService.validate(managedSubjectInstance)
    if(!validPassword) {
      log.error "New password is invalid for $managedSubjectInstance"
      
      flash.type = 'error'
      flash.message = 'controllers.aaf.vhr.account.completepasswordchange.new.password.invalid'
      render view: 'changepassword', model: [managedSubjectInstance:managedSubjectInstance]

      return
    }

    cryptoService.generatePasswordHash(managedSubjectInstance)
    flash.type = 'success'
    flash.message = 'controllers.aaf.vhr.account.completepasswordchange.new.password.success'
    redirect action: 'show'
  }
}
