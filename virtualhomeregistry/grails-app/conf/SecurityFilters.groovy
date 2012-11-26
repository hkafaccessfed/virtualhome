class SecurityFilters {

  def filters = {
    workflow_authenticated(uri:"/organisation/**") {
      before = {
        accessControl { true }
      }
    }
  }
}
