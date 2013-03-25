package aaf.vhr

class PopulateOrganizationsJob {
  def organizationService

  static triggers = {
    simple name: 'populateOrganizationsTrigger', startDelay: 1000 * 60 * 2, repeatInterval: 1000 * 60 * 60  // 2 minutes after start then hourly
  }

  def execute() {
    organizationService.populate()
  }
}
