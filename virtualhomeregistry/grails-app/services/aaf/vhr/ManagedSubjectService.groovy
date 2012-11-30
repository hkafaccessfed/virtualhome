package aaf.vhr

import org.springframework.context.i18n.LocaleContextHolder
import org.apache.commons.validator.EmailValidator

import aaf.base.admin.EmailTemplate

class ManagedSubjectService {

  boolean transactional = true
  def grailsApplication
  def messageSource

  def sharedTokenService
  def emailManagerService
  def passwordValidationService
  def cryptoService

  private final String TOKEN_COUNT = 'aaf.vhr.managedsubjectservice.registerfromcsv.invalidtokens'
  private final String TOKEN_CN = 'aaf.vhr.managedsubjectservice.registerfromcsv.invalidcn'
  private final String TOKEN_EMAIL = 'aaf.vhr.managedsubjectservice.registerfromcsv.invalidemail'
  private final String TOKEN_AFFILIATION ='aaf.vhr.managedsubjectservice.registerfromcsv.invalidaffiliation'

  private final String TOKEN_EMAIL_SUBJECT ='aaf.vhr.managedsubjectservice.registered.email.subject'

  private final String INVITATION_INVALID ='aaf.vhr.managedsubjectservice.finalize.invitation.invalid'

  public static final String[] AFFILIATIONS = [ 'faculty', 'student', 'staff', 'alum', 'member', 
                                                'affiliate', 'employee', 'library-walk-in' ]

  def finalize(ManagedSubjectInvitation invitation, String login, String plainPassword, String plainPasswordConfirmation) {
    if(invitation.utilized)
      return [false, messageSource.getMessage(INVITATION_INVALID, [] as Object[], INVITATION_INVALID, LocaleContextHolder.locale)]

    def managedSubject = invitation.managedSubject
    managedSubject.login = login
    managedSubject.plainPassword = plainPassword
    managedSubject.plainPasswordConfirmation = plainPasswordConfirmation
    
    def (valid, errors) = passwordValidationService.validate(managedSubject)
    if(!valid) {
      log.warn "Unable to finalize $managedSubject as password is invalid"
      return [false, managedSubject]
    }

    cryptoService.generatePasswordHash(managedSubject)
    managedSubject.active = true
    
    if(!managedSubject.save()) {
      log.error "Failed trying to save $managedSubject when finalizing"
      managedSubject.errors.each {
        log.warn it
      }

      throw new RuntimeException("Failed trying to save $managedSubject when finalizing")  // Rollback transaction
    }

    invitation.utilized = true
    if(!invitation.save()) {
      log.error "Failed trying to save $invitation when finalizing $managedSubject"
      managedSubject.errors.each {
        log.warn it
      }

      throw new RuntimeException("Failed trying to save $invitation when finalizing $managedSubject")  // Rollback transaction
    }

    log.info "Finalized the account for $managedSubject - they are now ready to use VHR"
    return [true, managedSubject]
  }

  def register(String cn, String email, String affiliation, Group group, boolean confirm = true) {
      def managedSubject = new ManagedSubject(cn:cn, email:email, active:false, organization:group.organization, group:group)
      sharedTokenService.generate(managedSubject)

      def eduPersonAffiliation = new AttributeValue(value:affiliation, attribute:Attribute.findWhere(oid:"1.3.6.1.4.1.5923.1.1.1.1"))
      managedSubject.addToPii(eduPersonAffiliation)

      if(!managedSubject.save()) {
        log.error "Failed trying to save $managedSubject"
        managedSubject.errors.each {
          log.warn it
        }

        throw new RuntimeException("Failed trying to save $managedSubject")  // Rollback transaction
      }

      if(confirm)
        sendConfirmation(managedSubject)

      managedSubject
  }

  def registerFromCSV(Group group, byte[] csv) {

    def emailValidator = EmailValidator.getInstance()

    def valid = true
    def errors = [] as List
    def subjects = [] as List

    def lc = 0

    ByteArrayInputStream is = new ByteArrayInputStream(csv)
    is.eachCsvLine { tokens ->
      lc++

      // Ensure required pii
      if(tokens.size() != 3) {
        valid = false
        errors.add(messageSource.getMessage(TOKEN_COUNT, [lc] as Object[], TOKEN_COUNT, LocaleContextHolder.locale))
      } else {
        // Only if we have the correct number of tokens do we look at actual content
        // Ensure cn format
        if(tokens[0].size() < 1 || tokens[0].count(' ') > 1) {
          valid = false
          errors.add(messageSource.getMessage(TOKEN_CN, [lc, tokens[0]] as Object[], TOKEN_CN, LocaleContextHolder.locale))
        }

        // Ensure email format
        if(tokens[1].size() < 1 || !emailValidator.isValid(tokens[1])) {
          valid = false
          errors.add(messageSource.getMessage(TOKEN_EMAIL, [lc, tokens[1]] as Object[], TOKEN_EMAIL, LocaleContextHolder.locale))
        }

        // Ensure affiliation
        if(tokens[2].size() < 1 || !ManagedSubjectService.AFFILIATIONS.contains(tokens[2])) {
          valid = false
          errors.add(messageSource.getMessage(TOKEN_AFFILIATION, [lc, tokens[2]] as Object[], TOKEN_AFFILIATION, LocaleContextHolder.locale))
        }
      }
    }

    is.close()  // a no-op but incase this changes in the future

    if(!valid) {
      log.error "Unable to process CSV uploaded by $subject"
      errors.each {
        log.info it
      }
      return [false, errors, null, lc]
    }

    // Valid CSV data, create ManagedSubjects
    lc = 0
    is = new ByteArrayInputStream(csv)
    is.eachCsvLine { tokens ->
      lc++

      def managedSubject = register(tokens[0], tokens[1], tokens[2], group, false)
      log.info "Created $managedSubject from CSV file submitted by $subject"
      subjects.add(managedSubject)
    }

    subjects.each { ms ->
      sendConfirmation(ms)
    }

    [true, errors, subjects, lc]
  }

  private void sendConfirmation(ManagedSubject managedSubject) {
    def emailSubject = messageSource.getMessage(TOKEN_EMAIL_SUBJECT, [] as Object[], TOKEN_EMAIL_SUBJECT, LocaleContextHolder.locale)
    def emailTemplate = EmailTemplate.findWhere(name:"registered_managed_subject")

    if(!emailTemplate) {
      throw new RuntimeException("Email template for creating new ManagedSubjects 'registered_managed_subject' does not exist")  // Rollback transaction
    }

    def invitation = new ManagedSubjectInvitation(managedSubject:managedSubject)
    if(!invitation.save()) {
      log.error "Failed to create invitation code for $managedSubject aborting"
      invitation.errors.each {
        log.warn it
      }
      throw new RuntimeException("Failed to create invitation code for $managedSubject aborting")  // Rollback transaction
    }
    emailManagerService.send(managedSubject.email, emailSubject, emailTemplate, [managedSubject:managedSubject, invitation:invitation]) 
  }

}
