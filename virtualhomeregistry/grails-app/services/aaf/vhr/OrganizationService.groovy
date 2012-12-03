package aaf.vhr

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.ResponseParseException
import net.sf.json.JSONException

import groovyx.net.http.ContentType
import groovyx.net.http.Method

import aaf.base.workflow.ProcessPriority

class OrganizationService {
  // A workflow process with the following name must be present in VHR
  static final CREATE_ORGANIZATION_WORKFLOW = 'Organization Creation Process'

  boolean transactional = true
  def grailsApplication
  def workflowProcessService

  /*
    Connects to remote FR instance and
    populates all Organization data into VHR.

    Where an Organization already exists we simply
    update it's record locally.

    No local management of Organizations outside of
    administrator assignment should ever be necessary.
    FR is the single source of truth for this data.
  */
  public void populate() {
    def server = grailsApplication.config.aaf.vhr.federationregistry.server
    def api = grailsApplication.config.aaf.vhr.federationregistry.api.organisations

    def organisations = queryOrganizations(server, api)

    if(organisations) {
      organisations.each { o ->
        def result = queryOrganization(server, o.link.replace(server, ''))
        if(result) {
          def org = Organization.findWhere(frID: o.id.longValue())
          if(org) {
            // Ensure we cache any updates coming down from FR
            org.name = result.name
            org.displayName = result.displayName
            org.description = !result.description.equals(null) ? result.description:null

            if(!org.undergoingWorkflow)
              org.active = result.functioning

            if(!org.save()) {
              log.error "Unable to update Organization instance ${org}"
              org.errors.each { error ->
                log.error error
              }
            }
          } else {
            if(result.functioning) {
              org = new Organization(name:result.name, displayName: result.displayName, frID: result.id)
              org.description = !result.description.equals(null) ? result.description:null
              org.undergoingWorkflow = true
              if(!org.save()) {
                log.error "Unable to save new Organization instance to represent ${result.name}"
                org.errors.each { error ->
                  log.error error
                }
              }

              // kickoff workflow
              def params = ['agentCN':'aaf.vhr.OrganizationService', 'agentEmail':'']
              def(created, processInstance) = workflowProcessService.initiate(OrganizationService.CREATE_ORGANIZATION_WORKFLOW, "Approve activation of $org", ProcessPriority.LOW, params)
              if(!created)
                log.error "Unable to create workflow process to approve creation of new $org"
              else {
                log.info "Created workflow process to approve creation of new $org"
                workflowProcessService.run(processInstance)
              }
            } else {
              log.warn "Not creating new Organization instance to represent ${result.name} as currently not functioning in Federation Registry"
            }
          }
        }
      }
    }
  }

  private def queryOrganizations(server, api) {
    def organisations = null
    try {
      def http = new HTTPBuilder(server)

      http.request(Method.GET, ContentType.JSON) {req ->
        uri.path = api
        contentType = 'application/json; charset=UTF-8'

        response.success = {resp, json ->
          log.info "Collected a total of ${json.organizations.size()} from Federation Registry ${server}${api}"
          organisations = json.organizations
        }

        response.failure = {resp ->
          log.error "Error requesting list of Organizations from Federation Registry ${server}${api}"
          log.error resp.statusLine
        }
      }
    } catch (JSONException jsonException) {
      log.error "JSONException requesting list of Organizations from Federation Registry ${server}${api}"
      log.error jsonException.message
    } catch (ResponseParseException parseException) {
      log.error "ResponseParseException requesting list of Organizations from Federation Registry ${server}${api}"
      log.error parseException.message
    } catch (Exception e) {
      log.error "Exception requesting list of Organizations from Federation Registry ${server}${api}"
      log.error e.message
    }  
    organisations
  }

  private def queryOrganization(server, api) {
    def organisation = null
    try {
      def http = new HTTPBuilder(server)

      http.request(Method.GET, ContentType.JSON) {req ->
        uri.path = api
        contentType = 'application/json; charset=UTF-8'

        response.success = {resp, json ->
          log.info "Collected ${json.organization} from Federation Registry ${server}${api}"
          organisation = json.organization
        }

        response.failure = {resp ->
          log.error "Error requesting list of Organization from Federation Registry ${server}${api}"
          log.error resp.statusLine
        }
      }
    } catch (JSONException jsonException) {
      log.error "JSONException requesting list of Organization from Federation Registry ${server}${api}"
      log.error jsonException.message
    } catch (ResponseParseException parseException) {
      log.error "ResponseParseException requesting list of Organization from Federation Registry ${server}${api}"
      log.error parseException.message
    } catch (Exception e) {
      log.error "Exception requesting list of Organization from Federation Registry ${server}${api}"
      log.error e.message
    }
    organisation  
  }
}
