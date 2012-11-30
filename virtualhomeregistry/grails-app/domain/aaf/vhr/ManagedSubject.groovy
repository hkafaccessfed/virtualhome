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

  // AAF Core
  String cn                   // oid:2.5.4.3
  String email                // oid:0.9.2342.19200300.100.1.3
  String sharedToken          // oid:1.3.6.1.4.1.27856.1.2.5
  String displayName          // oid:2.16.840.1.113730.3.1.241
  String eduPersonAssurance   // oid:1.3.6.1.4.1.5923.1.1.1.11
  String eduPersonAffiliation // oid:1.3.6.1.4.1.5923.1.1.1.1
  
  // AAF Optional
  String givenName            // oid:2.5.4.42
  String surname              // oid:2.5.4.4
  String mobileNumber         // oid:0.9.2342.19200300.100.1.41
  String telephoneNumber      // oid:2.5.4.20
  String postalAddress        // oid:2.5.4.16
  String organizationalUnit   // oid:2.5.4.11

  boolean active = false

  List challengeResponse
  List emailReset

  static hasMany = [challengeResponse: ChallengeResponse,
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
/*
    eduPersonAssurance inList: ['urn:mace:aaf.edu.au:iap:id:1',
                                'urn:mace:aaf.edu.au:iap:id:2'
                                'urn:mace:aaf.edu.au:iap:id:3'
                                'urn:mace:aaf.edu.au:iap:id:4']
*/
    eduPersonAffiliation inList: ['faculty',
                                  'student',
                                  'staff',
                                  'employee',
                                  'member',
                                  'affiliate',
                                  'alum',
                                  'library-walk-in']

    mobileNumber nullable: true, blank: false
    givenName nullable: true, blank: false          
    surname nullable: true, blank: false            
    telephoneNumber nullable: true, blank: false   
    postalAddress nullable: true, blank: false      
    organizationalUnit nullable: true, blank: false 

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
