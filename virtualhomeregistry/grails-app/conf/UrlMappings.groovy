class UrlMappings {

  static mappings = {

    "/"(controller:"dashboard", action:"welcome")

    "/myaccount/setup/login-available" {
      controller="finalization"
      action="loginAvailable"
    }
    "/myaccount/setup/complete" {
      controller="finalization"
      action="complete"
    }
    "/myaccount/setup/used" {
      controller="finalization"
      action="used"
    }
    "/myaccount/setup/$inviteCode" {
      controller="finalization"
    }

    "/dashboard"(controller:"dashboard", action:"dashboard")

    "/organisations/groups/accounts/$action/$id?" {
      controller="managedSubject"
    }
    "/organisations/groups/$action/$id?" {
      controller="group"
    }
    "/organisations/$action/$id?" {
      controller="organization"
    }

    "/backend/manageadministrators/$action/$id?" {
      controller="manageAdministrators"
    }
  }

}
