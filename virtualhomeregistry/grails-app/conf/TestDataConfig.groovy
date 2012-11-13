testDataConfig {
  sampleData {
   'aaf.vhr.ManagedSubject' {
      def i = 1
      login = {-> "loginname${i++}" }
      email = {-> "testuser${i++}@testdomain.com" }
      sharedToken = {-> "sharedtoken${i++}"}
    }
    'aaf.vhr.Group' {
      def i = 1
      name = {-> "groupname${i++}" }
      description = {-> "group $i description" }
    }
  }
}
