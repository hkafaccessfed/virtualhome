class UrlMappings {

  static mappings = {
    // Public Welcome, Internal Dashboard
    "/"(controller:"dashboard", action:"welcome")
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
