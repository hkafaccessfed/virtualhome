package aaf.vhr

class DashboardController {
  
  static defaultAction = "dashboard"

  def welcome = {
    if(subject) {
      redirect action:'dashboard'
    }
  }

  def dashboard = {
    def organizationInstanceList = [] as List
    def groupInstanceList = [] as List

    subject.roles?.each { role -> 
      if(role.name.startsWith('organization:')) {
        def components = role.name.split(':')
        if(components.size() == 3) {
          def organization = Organization.get(components[1])
          organizationInstanceList << organization
        }
      }
      if(role.name.startsWith('group:')) {
        def components = role.name.split(':')
        if(components.size() == 3) {
          def group = Group.get(components[1])
          groupInstanceList << group
        }
      }
    }

    def statistics = [:]
    statistics.organizations = Organization.count()
    statistics.groups = Group.count()
    statistics.managedSubjects = ManagedSubject.count()

    [organizationInstanceList:organizationInstanceList, groupInstanceList: groupInstanceList, statistics:statistics]
  }

}
