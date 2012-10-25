package aaf.vhr

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames=true, includeFields=true)
@EqualsAndHashCode
class AttributeValue {
  static auditable = true

  String value
  Attribute attribute

  static belongsTo = [subject:ManagedSubject]
  static constraints = {
    value  nullable: false, blank: false
  }
}
