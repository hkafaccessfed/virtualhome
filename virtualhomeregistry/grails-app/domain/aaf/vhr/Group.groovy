package aaf.vhr

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames=true, includes="id, name")
@EqualsAndHashCode
class Group {
  static auditable = true

  String name
  String description

  String welcomeMessage

  boolean active = true
  
  Date dateCreated
  Date lastUpdated

  static hasMany = [subjects: ManagedSubject]

  static belongsTo = [organization:Organization]

  static constraints = {
    name(nullable: false, blank: false)
    description (nullable: false, blank:false)
    dateCreated(nullable:true)
    lastUpdated(nullable:true)

    welcomeMessage(nullable:true, markup:true)
  }

  static mapping = {
    table 'vhr_group'
    welcomeMessage type: "text"
  }

  public boolean functioning() {
    active && organization.functioning()
  }
}
