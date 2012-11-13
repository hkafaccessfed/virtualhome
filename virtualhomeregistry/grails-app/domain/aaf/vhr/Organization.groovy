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
  long groupLimit
  
  boolean active = false
  boolean undergoingWorkflow = false

  static hasMany = [subjects: ManagedSubject,
                    groups: Group]
  
  Date dateCreated
  Date lastUpdated

  static constraints = {
    name(nullable: false, blank: false)
    displayName(nullable: false, blank: false)
    description(nullable:true, blank: false, maxSize:2000)

    dateCreated(nullable:true)
    lastUpdated(nullable:true)
  }

  public boolean functioning() {
    active && !undergoingWorkflow
  }

  public boolean canRegisterSubjects() {
    (subjectLimit == 0 || subjects.size() < subjectLimit) && functioning()
  }

  public boolean canRegisterGroups() {
    (groupLimit == 0 || groups.size() < groupLimit) && functioning()
  }

}
