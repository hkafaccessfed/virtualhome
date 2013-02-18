package aaf.vhr

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import aaf.base.identity.Subject

@ToString(includeNames=true, includes="id, login, cn, email")
@EqualsAndHashCode
class ManagedSubject {
  static auditable = true

  static final affiliations = [ 'affiliate',
                                'alum',
                                'employee',
                                'faculty',
                                'library-walk-in',
                                'member',
                                'staff',
                                'student'] as List

  String login
  String hash

  String apiKey               // Use for local account management context
  String eptidKey             // Used as input for EPTID generation so login changes don't impact us - should never be altered.

  // Password reset. Both codes required to be input, second provided via SMS or administrator
  String resetCode
  String resetCodeExternal           

  // AAF Core
  String cn                   // oid:2.5.4.3
  String email                // oid:0.9.2342.19200300.100.1.3
  String sharedToken          // oid:1.3.6.1.4.1.27856.1.2.5
  String displayName          // oid:2.16.840.1.113730.3.1.241
  String eduPersonAssurance   // oid:1.3.6.1.4.1.5923.1.1.1.11
  String eduPersonAffiliation // oid:1.3.6.1.4.1.5923.1.1.1.1 - stored seperated by ; for IdP resolver simplification
  String eduPersonEntitlement // oid:1.3.6.1.4.1.5923.1.1.1.7 - stored seperated by ; for IdP resolver simplification
  
  // AAF Optional
  String givenName            // oid:2.5.4.42
  String surname              // oid:2.5.4.4
  String mobileNumber         // oid:0.9.2342.19200300.100.1.41
  String telephoneNumber      // oid:2.5.4.20
  String postalAddress        // oid:2.5.4.16
  String organizationalUnit   // oid:2.5.4.11

  boolean active = false
  boolean locked = false
  boolean blocked = false
  boolean archived = false

  int failedLogins = 0
  int failedResets = 0

  List challengeResponse
  List emailReset

  static hasMany = [challengeResponse: ChallengeResponse,
                    emailReset: EmailReset,
                    invitations: ManagedSubjectInvitation,
                    activeChanges: ManagedSubjectStateChange,
                    lockedChanges: ManagedSubjectStateChange,
                    archivedChanges: ManagedSubjectStateChange,
                    blockedChanges: ManagedSubjectStateChange]  

  static belongsTo = [organization:Organization,
                      group:Group]

  static constraints = {
    login nullable:true, blank: false, unique: true, size: 3..100,  validator: { val -> if (val?.contains(' ')) return 'value.contains.space' }
    hash nullable:true, blank:false, minSize:60, maxSize:60
    
    resetCode nullable:true
    resetCodeExternal nullable:true

    email blank:false, unique:true, email:true
    cn validator: {val, obj ->
      return (val != null && val != '' && (val.count(' ') == 0 || val.count(' ') == 1))
    }
    sharedToken nullable:false, blank: false, unique: true
    eduPersonEntitlement nullable:true, blank:false

    eduPersonAssurance inList: ['urn:mace:aaf.edu.au:iap:id:1',
                                'urn:mace:aaf.edu.au:iap:id:2',
                                'urn:mace:aaf.edu.au:iap:id:3',
                                'urn:mace:aaf.edu.au:iap:id:4']

    eduPersonAffiliation nullable:false, blank:false, maxSize: 255

    mobileNumber nullable: true, blank: false, validator: validMobileNumber
    givenName nullable: true, blank: false          
    surname nullable: true, blank: false            
    telephoneNumber nullable: true, blank: false   
    postalAddress nullable: true, blank: false      
    organizationalUnit nullable: true, blank: false 

    organization nullable: false
    group nullable: false
  }

  static mapping = {
    eduPersonEntitlement type: "text"
  }

  public ManagedSubject() {
    this.eptidKey = aaf.vhr.crypto.CryptoUtil.randomAlphanumeric(12)
    this.apiKey = aaf.vhr.crypto.CryptoUtil.randomAlphanumeric(16)
  }

  String plainPassword
  String plainPasswordConfirmation
  static transients = ['plainPassword', 'plainPasswordConfirmation']

  public canChangePassword() {
    !locked && !blocked && !archived && organization?.functioning() && group?.functioning()
  }

  public boolean functioning() {
    active && !locked && !blocked && !archived && organization?.functioning() && group?.functioning()
  }

  public void setEptidKey(String eptidKey) {
    log.error ("Unable to set eptidKey. It is created automatically and is immutable.")
  }

  public void setResetCode(String resetCode) {
    if(resetCode)
      this.resetCode = cleanCode(resetCode)
    else
      this.resetCode = null
  }

  public void setResetCodeExternal(String resetCodeExternal) {
    if(resetCodeExternal)
      this.resetCodeExternal = cleanCode(resetCodeExternal)
    else
      this.resetCodeExternal = null
  }

  public lock(String reason, String category, String environment, Subject actionedBy) {
    this.locked = true

    def lockChange = new ManagedSubjectStateChange(event:StateChangeType.LOCKED, reason:reason, category:category, environment:environment, actionedBy:actionedBy)
    this.addToLockedChanges(lockChange)

    if(!this.save(flush:true)) {
      log.error "Unable to save $this when setting locked state"
      this.errors.each {
        log.error it
      }
      throw new RuntimeException ("Unable to save $this when setting locked state")
    }
  }

  public unlock(String reason, String category, String environment, Subject actionedBy) {
    this.locked = false
    this.failedResets = 0

    def lockChange = new ManagedSubjectStateChange(event:StateChangeType.UNLOCKED, reason:reason, category:category, environment:environment, actionedBy:actionedBy)
    this.addToLockedChanges(lockChange)

    if(!this.save(flush:true)) {
      log.error "Unable to save $this when setting unlocked state"
      this.errors.each {
        log.error it
      }
      throw new RuntimeException ("Unable to save $this when setting unlocked state")
    }
  }

  public block(String reason, String category, String environment, Subject actionedBy) {
    this.blocked = true

    def blockChange = new ManagedSubjectStateChange(event:StateChangeType.BLOCKED, reason:reason, category:category, environment:environment, actionedBy:actionedBy)
    this.addToBlockedChanges(blockChange)

    if(!this.save(flush:true)) {
      log.error "Unable to save $this when setting blocked state"
      this.errors.each {
        log.error it
      }
      throw new RuntimeException ("Unable to save $this when setting blocked state")
    }
  }

  public unblock(String reason, String category, String environment, Subject actionedBy) {
    this.blocked = false
    this.failedResets = 0

    def blockChange = new ManagedSubjectStateChange(event:StateChangeType.UNBLOCKED, reason:reason, category:category, environment:environment, actionedBy:actionedBy)
    this.addToBlockedChanges(blockChange)

    if(!this.save(flush:true)) {
      log.error "Unable to save $this when setting unblocked state"
      this.errors.each {
        log.error it
      }
      throw new RuntimeException ("Unable to save $this when setting unblocked state")
    }
  }

  public activate(String reason, String category, String environment, Subject actionedBy) {
    this.active = true
    this.failedLogins = 0

    def change = new ManagedSubjectStateChange(event:StateChangeType.ACTIVATE, reason:reason, category:category, environment:environment, actionedBy:actionedBy)
    this.addToActiveChanges(change)

    if(!this.save(flush:true)) {
      log.error "Unable to save $this when setting active state"
      this.errors.each {
        log.error it
      }
      throw new RuntimeException ("Unable to save $this when setting active state")
    }
  }

  public deactivate(String reason, String category, String environment, Subject actionedBy) {
    this.active = false

    def change = new ManagedSubjectStateChange(event:StateChangeType.DEACTIVATE, reason:reason, category:category, environment:environment, actionedBy:actionedBy)
    this.addToActiveChanges(change)

    if(!this.save(flush:true)) {
      log.error "Unable to save $this when setting deactive state"
      this.errors.each {
        log.error it
      }
      throw new RuntimeException ("Unable to save $this when setting deactive state")
    }
  }

  public archive(String reason, String category, String environment, Subject actionedBy) {
    this.archived = true

    def change = new ManagedSubjectStateChange(event:StateChangeType.ARCHIVED, reason:reason, category:category, environment:environment, actionedBy:actionedBy)
    this.addToArchivedChanges(change)

    if(!this.save(flush:true)) {
      log.error "Unable to save $this when setting archive state"
      this.errors.each {
        log.error it
      }
      throw new RuntimeException ("Unable to save $this when setting archive state")
    }
  }

  public unarchive(String reason, String category, String environment, Subject actionedBy) {
    this.archived = false

    def change = new ManagedSubjectStateChange(event:StateChangeType.UNARCHIVED, reason:reason, category:category, environment:environment, actionedBy:actionedBy)
    this.addToArchivedChanges(change)

    if(!this.save(flush:true)) {
      log.error "Unable to save $this when setting deactive state"
      this.errors.each {
        log.error it
      }
      throw new RuntimeException ("Unable to save $this when setting deactive state")
    }
  }

  public increaseFailedResets() {
    this.failedResets++

    if(!this.save(flush:true)) {
      log.error "Unable to save $this when increasing failed logins"
      this.errors.each {
        log.error it
      }
      throw new RuntimeException ("Unable to save $this when increasing failed logins")
    }
  }

  private String cleanCode(String code) {
    // Ensure no confusion on SMS/Email codes between
    // characters that look the same - extend as

    if (code.contains('I')) {
      code = code.replace('I', 'i')
    }
    if(code.contains('l')) {
      code = code.replace('l', 'L')
    }
    if(code.contains('O')) {
      code = code.replace('O', 'o')
    }
    if(code.contains('0')) {
      code = code.replace('0', '9')
    }

    code
  }

  static validMobileNumber = { value, obj ->
    if(value == "" || value == null) {
      obj.mobileNumber = null
      return true
    }

    def checkedNumber = value

    // Translate Australian numbers to international format
    if(checkedNumber.startsWith('04')) {
      checkedNumber = checkedNumber[1..-1]
      checkedNumber = "+61$checkedNumber"
    }

    if(!checkedNumber.startsWith('+')) {
      return false
    } else {
      obj.mobileNumber = checkedNumber
      return true
    }
  }
}
