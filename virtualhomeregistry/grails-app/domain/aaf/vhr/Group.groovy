package aaf.vhr

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames=true, includeFields=true)
@EqualsAndHashCode
class Group {
  static auditable = true

  String name
  String description
  
  Date dateCreated
  Date lastUpdated

  static hasMany = [subjects: ManagedSubject]

  static belongsTo = [organization:Organization]

  static constraints = {
    name(nullable: false, blank: false)
    description (nullable: false, blank:false)
    dateCreated(nullable:true)
    lastUpdated(nullable:true)
  }
}
