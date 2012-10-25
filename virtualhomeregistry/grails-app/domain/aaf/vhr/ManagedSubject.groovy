package aaf.vhr

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import aaf.base.identity.Subject

@ToString(includeNames=true, includeFields=true, excludes="pii, hash, plainPassword, plainPasswordConfirmation")
@EqualsAndHashCode
class ManagedSubject {
  String login
  String hash
  
  List pii

  static hasMany = [pii: AttributeValue]  // Personally Identifiable Information (PII)

  static constraints = {
    login nullable:true, blank: false, unique: true, size: 5..100
    hash blank:false, minSize:60, maxSize:60
  }

  String plainPassword
  String plainPasswordConfirmation
  static transients = ['plainPassword', 'plainPasswordConfirmation']
}
