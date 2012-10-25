package aaf.vhr

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import aaf.base.identity.Subject

@ToString(includeNames=true, includeFields=true, excludes="sharedToken, hash, plainPassword, plainPasswordConfirmation")
@EqualsAndHashCode
class ManagedSubject extends Subject {
  String login
  String hash

  String plainPassword
  String plainPasswordConfirmation
  
  static transients = ['plainPassword', 'plainPasswordConfirmation']

  static constraints = {
    principal nullable: true
    login nullable:false, blank: false, unique: true, size: 6..100
    hash blank:false, minSize:60, maxSize:60
  }
}
