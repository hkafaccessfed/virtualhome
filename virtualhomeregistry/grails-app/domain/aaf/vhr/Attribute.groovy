package aaf.vhr

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames=true, includeFields=true)
@EqualsAndHashCode
class Attribute {
  static auditable = true

  String name
  String oid
  String description
  
  Date dateCreated
  Date lastUpdated

  static constraints = {
    name(nullable: false, blank: false, unique: true)
    oid(nullable: false, blank:false)
    description (nullable: false, blank:false)
    dateCreated(nullable:true)
    lastUpdated(nullable:true)
  }
}
