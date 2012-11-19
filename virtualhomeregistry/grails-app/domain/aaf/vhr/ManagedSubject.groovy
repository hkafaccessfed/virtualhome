package aaf.vhr

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import aaf.base.identity.Subject

@ToString(includeNames=true, includeFields=true, excludes="pii, hash, plainPassword, plainPasswordConfirmation")
@EqualsAndHashCode
class ManagedSubject {
  static auditable = true

  String login
  String hash

  // Frequently accessed PII is stored specifically
  // All other PII accessible via ManagedSubject.pii list
  String cn           // per oid:2.5.4.3
  String email        // per oid:0.9.2342.19200300.100.1.3
  String sharedToken  // per oid:1.3.6.1.4.1.27856.1.2.5
  String mobileNumber // per oid:0.9.2342.19200300.100.1.41
  
  boolean active = true

  List pii
  List challengeResponse
  List emailReset

  static hasMany = [pii: AttributeValue,  // Personally Identifiable Information (PII)
                    challengeResponse: ChallengeResponse,
                    emailReset: EmailReset]  

  static belongsTo = [organization:Organization,
                      group:Group]

  static constraints = {
    login nullable:true, blank: false, unique: true, size: 5..100
    hash nullable:true, blank:false, minSize:60, maxSize:60
    email blank:false, unique:true, email:true
    cn validator: {val, obj ->
      return (val != null && val != '' && (val.count(' ') == 0 || val.count(' ') == 1))
    }
    sharedToken nullable:false, blank: false, unique: true
    mobileNumber nullable: true, blank: false

    organization nullable: false
    group nullable: false
  }

  String plainPassword
  String plainPasswordConfirmation
  static transients = ['plainPassword', 'plainPasswordConfirmation']

  public boolean functioning() {
    active && organization?.functioning() && group?.functioning()
  }
}
