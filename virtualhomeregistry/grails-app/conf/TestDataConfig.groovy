testDataConfig {
  sampleData {
   'aaf.vhr.ManagedSubject' {
      def i = 1
      login = {-> "loginname${i++}" }
      email = {-> "testuser${i++}@domain.com" }
      sharedToken = {-> "sharedtoken${i++}"}
    }
  }
}
