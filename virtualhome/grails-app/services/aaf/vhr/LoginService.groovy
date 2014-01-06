package aaf.vhr

import org.springframework.beans.factory.InitializingBean
import java.util.concurrent.TimeUnit
import com.google.common.cache.*

import aaf.vhr.crypto.GoogleAuthenticator

class LoginService implements InitializingBean{

  boolean transactional = true
  def grailsApplication

  def recaptchaService
  def cryptoService

  int validityPeriod = 2  // Defaults to 2 minutes, override in config

  Cache<String, String> loginCache

  void afterPropertiesSet()  {
    if(grailsApplication.config.aaf.vhr.login.validity_period_minutes)
      validityPeriod = grailsApplication.config.aaf.vhr.login.validity_period_minutes

    //initialize the loginCache
    loginCache = CacheBuilder.newBuilder().
    expireAfterWrite(validityPeriod, TimeUnit.MINUTES).
    maximumSize(1000).
    build()
  }

  public String sessionRemoteUser(String sessionID) {
    loginCache.getIfPresent(sessionID)
  }

  public boolean passwordLogin(ManagedSubject managedSubjectInstance, String password, def request, def session, def params) {
    if(!managedSubjectInstance.canLogin()) {
      String reason = "User attempted login but account is disabled."
      String requestDetails = createRequestDetails(request)

      managedSubjectInstance.failLogin(reason, 'login_attempt', requestDetails, null)

      log.error "The ManagedSubject $managedSubjectInstance can not login at this time due to inactivty or locks"
      return false
    }

    if( managedSubjectInstance.requiresLoginCaptcha() && !recaptchaService.verifyAnswer(session, request.getRemoteAddr(), params)) {
      String reason = "User provided invalid captcha data."
      String requestDetails = createRequestDetails(request)

      managedSubjectInstance.failCaptcha(reason, 'login_attempt', requestDetails, null)

      log.error "The recaptcha data supplied for ManagedSubject $managedSubjectInstance is not correct"
      return false
    }

    if(!cryptoService.verifyPasswordHash(password, managedSubjectInstance)) {
      String reason = "User provided an incorrect password."
      String requestDetails = createRequestDetails(request)

      managedSubjectInstance.failLogin(reason, 'login_attempt', requestDetails, null)

      log.error "The password supplied for ManagedSubject $managedSubjectInstance is not correct"
      return false
    }

    log.info "The password supplied for ManagedSubject $managedSubjectInstance was valid."

    String reason = "User provided correct password at login."
    String requestDetails = createRequestDetails(request)
    managedSubjectInstance.successfulLogin(reason, 'login_attempt', requestDetails, null)

    true
  }

  public boolean totpLogin(ManagedSubject managedSubjectInstance, long code, def request) {
    if(!managedSubjectInstance.canLogin()) {
      String reason = "User attempted login but account is disabled (TOTP)."
      String requestDetails = createRequestDetails(request)

      managedSubjectInstance.failLogin(reason, 'login_attempt', requestDetails, null)

      log.error "The ManagedSubject $managedSubjectInstance can not use login at this time due to inactivty or locks (TOTP)."
      return false
    }

    if(!GoogleAuthenticator.checkCode(managedSubjectInstance.totpKey, code, System.currentTimeMillis())) {
      String reason = "User provided invalid code for 2-Step verification."
      String requestDetails = createRequestDetails(request)

      managedSubjectInstance.failLogin(reason, 'login_attempt', requestDetails, null)

      log.error "The TOTP code supplied for ManagedSubject $managedSubjectInstance is not correct or does not match the stored totpKey."
      return false
    }

    log.info "The TOTP code supplied for ManagedSubject $managedSubjectInstance was valid."

    String reason = "User provided valid code for 2-Step verification."
    String requestDetails = createRequestDetails(request)
    managedSubjectInstance.successfulLogin(reason, 'login_attempt', requestDetails, null)

    true
  }

  public String establishSession(ManagedSubject managedSubjectInstance) {
    def sessionID = aaf.vhr.crypto.CryptoUtil.randomAlphanumeric(64)
    loginCache.put(sessionID, managedSubjectInstance.login)

    sessionID
  }

  private String createRequestDetails(def request) {
"""User Agent: ${request.getHeader('User-Agent')}
Remote Host: ${request.getRemoteHost()}
Remote IP: ${request.getRemoteAddr()}
URI: ${request.requestURI}"""
  }
}
