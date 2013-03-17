import aaf.vhr.*

import aaf.base.identity.*
import aaf.base.admin.*
import aaf.base.workflow.*

import org.apache.shiro.subject.Subject
import org.apache.shiro.util.ThreadContext
import org.apache.shiro.SecurityUtils

import grails.util.Environment

class BootStrap {

  def grailsApplication
  def workflowProcessService

    def init = { servletContext ->
    
      if(Environment.current != Environment.TEST) {

        def subject = aaf.base.identity.Subject.findWhere(principal:"aaf.base.identity:internal_account")
        if(!subject) {
          throw new RuntimeException("Unable to retrieve initial subject reference \
            'aaf.base.identity:internal_account' which should be populated by base")
        }

        def organization_approval = WorkflowScript.findWhere(name:'organization_approval')
        if(!organization_approval) {
          def scriptMarkup = grailsApplication.parentContext.getResource("classpath:aaf/vhr/organization_approval.scr").inputStream.text
          organization_approval = new WorkflowScript(name:'organization_approval', description:'Executed to finalize new Organization', definition:scriptMarkup, , processVersion:1, creator:subject)
          if(!organization_approval.save()) {
            organization_approval.errors.each {
              println it
            }
            throw new RuntimeException("Unable to populate initial workflow script organization_approval")
          }
        }

        def organization_creation_process = Process.findWhere(name:'organization_creation_process')
        if(!organization_creation_process) {

          def suMetaClass = new ExpandoMetaClass(SecurityUtils)
          suMetaClass.'static'.getSubject = {[getPrincipal:{subject.id}] as Subject}
          suMetaClass.initialize()
          SecurityUtils.metaClass = suMetaClass

          def processMarkup = grailsApplication.parentContext.getResource("classpath:aaf/vhr/organization_creation_workflow.pr").inputStream.text
          workflowProcessService.create(processMarkup)

          SecurityUtils.metaClass = null
        }

        def registered_managed_subject = EmailTemplate.findWhere(name:'registered_managed_subject') 
        if(!registered_managed_subject) {
          def templateMarkup = grailsApplication.parentContext.getResource("classpath:aaf/vhr/registered_managed_subject.gsp").inputStream.text
          registered_managed_subject = new EmailTemplate(name:'registered_managed_subject', content: templateMarkup)
          if(!registered_managed_subject.save()) {
            registered_managed_subject.errors.each {
              println it
            }
            throw new RuntimeException("Unable to populate initial user registration email template registered_managed_subject")
          }
        }

        
        def approved_new_organization = EmailTemplate.findWhere(name:'approved_new_organization') 
        if(!approved_new_organization) {
          def templateMarkup = grailsApplication.parentContext.getResource("classpath:aaf/vhr/approved_new_organization.gsp").inputStream.text
          approved_new_organization = new EmailTemplate(name:'approved_new_organization', content: templateMarkup)
          if(!approved_new_organization.save()) {
            approved_new_organization.errors.each {
              println it
            }
            throw new RuntimeException("Unable to populate initial user registration email template approved_new_organization")
          }
        }
      }

      def email_password_code = EmailTemplate.findWhere(name:'email_password_code') 
      if(!email_password_code) {
        def templateMarkup = grailsApplication.parentContext.getResource("classpath:aaf/vhr/email_password_code.gsp").inputStream.text
        email_password_code = new EmailTemplate(name:'email_password_code', content: templateMarkup)
        if(!email_password_code.save()) {
          email_password_code.errors.each {
            println it
          }
          throw new RuntimeException("Unable to populate initial user registration email template email_password_code")
        }
      }
    }

    def destroy = {
    }
}
