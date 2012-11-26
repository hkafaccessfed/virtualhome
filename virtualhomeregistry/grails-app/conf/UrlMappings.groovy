class UrlMappings {

  static mappings = {
    // Public Welcome, Internal Dashboard
    "/"(controller:"dashboard", action:"welcome")
    "/dashboard"(controller:"dashboard", action:"dashboard")

    "/organisation/groups/$action/$id?" {
      controller="group"
    }
    "/organisation/$action/$id?" {
      controller="organization"
    }
  }

}
