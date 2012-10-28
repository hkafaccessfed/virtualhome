package aaf.vhr

import org.apache.shiro.crypto.hash.Sha512Hash
import aaf.vhr.crypto.BCrypt

class CryptoService {
  boolean transactional = true
  def grailsApplication

  /*
    The modular crypt format for bcrypt consists of:

    1.  $2a$ identifying the hashing algorithm and format
    2.  A two digit value denoting the cost parameter, followed by $
    3.  A 53 characters long base-64-encoded value (using the alphabet ., /, 0–9, A–Z, a–z 
        that is different to the standard Base 64 Encoding alphabet) consisting of:
          22 characters of salt (effectively only 128 bits)
          31 characters of encrypted output (effectively only 184 bits)
    
    Thus the total length is 60 bytes.

    There is a limit on the number of characters of the incoming password BCrypt will utilise.
    The BCrypt implementation XOR using P_orig which is 184 bytes integer until it gets to the end, 
    which limits the encryption "key" to 72 bytes. 
    Everything after 72 bytes is ignored (a warning would have been nice).

    Hoever we don't limit the user's password to 72 characters or less but simply 
    let it pass silently. The idea behind this being that a 72 characters bCrypted password 
    is better than the fast hashing alternative.

    For gensalt we can utilise a value of between 4 and 31. This should be increased in
    configuration over time as server power increases.

    @pre: Subject.plainPassword is non null, non blank and meets any min length requirements
    of the caller.
  */
  public void generatePasswordHash(ManagedSubject subject) {
    def salt = BCrypt.gensalt(grailsApplication.config.aaf.vhr.crypto.log_rounds)
    def hash = BCrypt.hashpw(subject.plainPassword, salt)
    subject.hash = hash
  }

  /*
    @pre: plainPassword is non null, subject.hash is populated and was created by generatePasswordHash
  */
  public boolean verifyPasswordHash(String plainPassword, ManagedSubject subject) {
    BCrypt.checkpw(plainPassword, subject.hash)
  }

  /*
    SHA-512 for hashing challenge response answers.

    @pre: challengeResponse.response is non null, non blank and meets any min length requirements
    of the caller.
  */
  public void generateChallengeResponseHash(ChallengeResponse challengeResponse) {
    // We use BCrypt salt generation for convenience
    challengeResponse.salt = BCrypt.gensalt(grailsApplication.config.aaf.vhr.crypto.log_rounds)
    def hash = new Sha512Hash(challengeResponse.response, challengeResponse.salt, grailsApplication.config.aaf.vhr.crypto.sha_rounds)  
    challengeResponse.hash = hash.toString()
  }

  /*
    Verify using SHA-512 for challenge response answers.

    @pre: challengeResponse is non null, non blank and meets any min length requirements
    of the caller.
  */
  public boolean verifyChallengeResponseHash(String response, ChallengeResponse challengeResponse) {
    def hash = new Sha512Hash(response, challengeResponse.salt, grailsApplication.config.aaf.vhr.crypto.sha_rounds)  
    challengeResponse.hash == hash.toString()
  }

  /*
    SHA-512 for hashing email reset codes.

    @pre: emailReset is non null
  */
  public void generateEmailResetHash(EmailReset emailReset) {
    emailReset.code = org.apache.commons.lang.RandomStringUtils.randomAlphanumeric(24)
    // We use BCrypt salt generation for convenience
    emailReset.salt = BCrypt.gensalt(grailsApplication.config.aaf.vhr.crypto.log_rounds)
    def hash = new Sha512Hash(emailReset.code, emailReset.salt, grailsApplication.config.aaf.vhr.crypto.sha_rounds)  
    emailReset.hash = hash.toString()
  }

  /*
    Verify using Sha512 for email reset codes.

    @pre: emailReset is non null and has subject it is associated with populated
    by the caller.
  */
  public boolean verifyEmailResetHash(String code, EmailReset emailReset) {
    def hash = new Sha512Hash(code, emailReset.salt, grailsApplication.config.aaf.vhr.crypto.sha_rounds)  
    emailReset.hash == hash.toString()
  }

}
