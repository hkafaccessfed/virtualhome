package aaf.vhr

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames=true, includeFields=true, excludes="hash, response")
@EqualsAndHashCode
class ChallengeResponse {
  static auditable = true

  String challenge
  String hash
  
  Date dateCreated
  Date lastUpdated

  static belongsTo = [subject:ManagedSubject]
  static constraints = {
    challenge nullable: false, blank: false, minSize:6   
    hash nullable: false, blank:false, size: 128..128
  }

  String response
  static transients = ['response']
}
