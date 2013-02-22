package aaf.vhr

import org.springframework.context.i18n.LocaleContextHolder
import org.apache.commons.validator.EmailValidator

import groovy.time.TimeCategory

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
  private final String TOKEN_EXPIRY ='aaf.vhr.managedsubjectservice.registerfromcsv.expiry'

  private final String TOKEN_EMAIL_SUBJECT ='aaf.vhr.managedsubjectservice.registered.email.subject'

  private final String INVITATION_INVALID ='aaf.vhr.managedsubjectservice.finalize.invitation.invalid'

  public static final String[] AFFILIATIONS = [ 'faculty', 'student', 'staff', 'alum', 'member', 
                                                'affiliate', 'employee', 'library-walk-in' ]

  public static final String DEFAULT_ASSURANCE = 'urn:mace:aaf.edu.au:iap:id:1'

  def finalize(ManagedSubjectInvitation invitation, String login, String plainPassword, String plainPasswordConfirmation, String mobileNumber) {
    if(invitation.utilized)
      return [false, messageSource.getMessage(INVITATION_INVALID, [] as Object[], INVITATION_INVALID, LocaleContextHolder.locale)]

    def managedSubject = invitation.managedSubject
    managedSubject.login = login
    managedSubject.plainPassword = plainPassword
    managedSubject.plainPasswordConfirmation = plainPasswordConfirmation
    managedSubject.mobileNumber = mobileNumber
    
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

  def register(ManagedSubject managedSubject, boolean confirm = true) {
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
      if(tokens.size() != 4) {
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

        // Ensure expiry
        if(tokens[3].size() < 1 || !tokens[3].isNumber()){
          valid = false
          errors.add(messageSource.getMessage(TOKEN_EXPIRY, [lc, tokens[2]] as Object[], TOKEN_EXPIRY, LocaleContextHolder.locale))
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

      def managedSubject = new ManagedSubject(cn:tokens[0], email:tokens[1], eduPersonAffiliation:tokens[2], active:false, displayName:tokens[0], eduPersonAssurance: DEFAULT_ASSURANCE, organization:group.organization, group:group)
      sharedTokenService.generate(managedSubject)

      if(tokens[3].toInteger() > 0) {
        use(TimeCategory) {
          Date now = new Date()
          managedSubject.accountExpires = now + tokens[3].toInteger().months
        }
      }

      if(!managedSubject.validate()) {
        valid = false
      }

      subjects.add(managedSubject)
    }

    if(valid) {
      // There is of course a small chance that someone else could create a ManagedSubject
      // with the same values in the interim but there is minimal chance so we're not
      // going to go nuts with locks. If it does happen the user will get a 500 and everything gets rolled back
      subjects.each { managedSubject ->
        managedSubject = register(managedSubject, false)
        log.info "Created $managedSubject from CSV file submitted by $subject"
      }

      log.info "Created all subjects from CSV file submitted by $subject, emailing welcome messages"
      subjects.each { ms ->
        sendConfirmation(ms)
      }

      return [true, errors, subjects, lc]
    }
    
    [false, errors, subjects, lc]
  }

  public void sendConfirmation(ManagedSubject managedSubject) {
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
