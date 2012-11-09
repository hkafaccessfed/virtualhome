package aaf.vhr

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import aaf.base.identity.Role

@ToString(includeNames=true, includeFields=true, excludes="description, url, frURL")
@EqualsAndHashCode
class Organization  {
  static auditable = true

  String name
  String displayName
  String description

  long frID
  long subjectLimit 
  long roleLimit
  
  boolean active = false

  static hasMany = [subjects: ManagedSubject,
                    roles: Role]
  
  Date dateCreated
  Date lastUpdated

  static constraints = {
    name(nullable: false, blank: false)
    displayName(nullable: false, blank: false)
    description(nullable:true, blank: false, maxSize:2000)

    dateCreated(nullable:true)
    lastUpdated(nullable:true)
  }

  public boolean canRegisterSubjects() {
    (subjectLimit == 0 || subjects.size() < subjectLimit) && active
  }

  public boolean canRegisterRoles() {
    (roleLimit == 0 || subjects.size() < roleLimit) && active
  }

}
