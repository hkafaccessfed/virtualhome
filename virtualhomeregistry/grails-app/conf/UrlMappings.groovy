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
    "/myaccount" {
      controller="account"
      action="index"
    }
    "/myaccount/logout" {
      controller="account"
      action="logout"
    }
    "/myaccount/details" {
      controller="account"
      action="show"
    }
    "/myaccount/changepassword" {
      controller="account"
      action="changepassword"
    }
    "/myaccount/completepasswordchange" {
      controller="account"
      action="completepasswordchange"
    }
    "/lostpassword/$action" {
      controller="lostPassword"
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

    "/api/v1/managedsubject/$action" {
      controller="managedSubjectApi"
    }
  }

}
