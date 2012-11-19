package aaf.vhr

class ManagedSubjectInvitation {
  static auditable = true

  String inviteCode
  ManagedSubject managedSubject
  boolean utilized

  Date dateCreated
  Date lastUpdated

  public ManagedSubjectInvitation() {
    this.inviteCode = org.apache.commons.lang.RandomStringUtils.randomAlphanumeric(24)
    this.utilized = false
  }
  
  static constraints = {
    managedSubject(nullable:false)
    inviteCode(nullable:false, unique:true)
  }
}
