class SecurityFilters {

  def filters = {
    dashboard_authenticated(uri:"/dashboard/**") {
      before = {
        accessControl { true }
      }
    }
    workflow_authenticated(uri:"/organisation/**") {
      before = {
        accessControl { true }
      }
    }
  }
}
