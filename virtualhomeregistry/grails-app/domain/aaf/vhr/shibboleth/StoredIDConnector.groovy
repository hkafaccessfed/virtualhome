package aaf.vhr.shibboleth

/* 
This is really only for Shibboleth IdP.
https://wiki.shibboleth.net/confluence/display/SHIB2/ResolverStoredIDDataConnector

However referenced here incase we wish to expose it in
the VHO UI sometime in the future.
*/
class StoredIDConnector {
  String localEntity
  String peerEntity
  String principalName
  String localId
  String persistentId
  String peerProvidedId

  Date creationDate
  Date deactivationDate

  static constraints = {
    peerProvidedId nullable:true
    deactivationDate nullable:true
  }

  static mapping = {
    table 'shibpid'

    principalName column:'principalName'
    peerProvidedId column:'peerProvidedId'

    persistentId column:'persistentId', index:'persistentId_idx,persistentId2_idx'
    localEntity column:'localEntity', index:'localEntity_idx,localEntity2_idx'
    peerEntity column:'peerEntity', index:'localEntity_idx,localEntity2_idx'
    localId column:'localId', index:'localEntity_idx,localEntity2_idx'

    creationDate column:'creationDate', sqlType: "timestamp"
    deactivationDate column:'deactivationDate', index:'persistentId2_idx,localEntity2_idx', sqlType: "timestamp"
  }
}
