package aaf.vhr

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import aaf.base.identity.Subject

@ToString(includeNames=true, includes="id, reason")
@EqualsAndHashCode
class ManagedSubjectStateChange {

  String reason

  String category
  String environment  // unique information about the environment at the time the change was made

  Subject actionedBy

  Date dateCreated
  Date lastUpdated

  static belongsTo = [subject:ManagedSubject]

  static constraints = {
    actionedBy nullable:true
    category nullable:true
    environment nullable:true
  }

  static mapping = {
    environment type: "text"
  }
}
