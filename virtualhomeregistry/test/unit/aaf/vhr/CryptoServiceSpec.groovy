package aaf.vhr

import grails.test.mixin.*
import grails.buildtestdata.mixin.Build
import spock.lang.*
import grails.plugin.spock.*

import aaf.vhr.ManagedSubject

@TestFor(aaf.vhr.CryptoService)
@Build([ManagedSubject, ChallengeResponse])
class CryptoServiceSpec extends UnitSpec {

  def cs

  def setup() {
    cs = new CryptoService(grailsApplication: grailsApplication)
    grailsApplication.config.aaf.vhr.crypto.log_rounds = 12
  }

  def 'validate various passwords store and confirm correctly'() {
    setup:
    def subject = ManagedSubject.build()
    subject.hash = null
    subject.plainPassword = pw
    subject.plainPasswordConfirmation = pw

    expect:
    subject.hash == null

    when:
    cs.generateBCryptHash(subject)

    then:
    subject.hash.size() == 60
    subject.validate()
    cs.verifyBCryptHash(pw, subject)

    where:
    pw << ['today123', 'I really enjoy XKCD.com 936 style PasswordS', 'XXYfgLvCehJ6qjflkMRBZ99Jw2I=']
  }

  def 'validate various passwords fail if not entered correctly'() {
    setup:
    def subject = ManagedSubject.build()
    subject.hash = null
    subject.plainPassword = pw
    subject.plainPasswordConfirmation = pw

    expect:
    subject.hash == null

    when:
    cs.generateBCryptHash(subject)

    then:
    subject.hash.size() == 60
    subject.validate()
    !cs.verifyBCryptHash(plainPW, subject)

    where:
    pw << ['today123', 'I really enjoy XKCD.com 936 style PasswordS', 'XXYfgLvCehJ6qjflkMRBZ99Jw2I=']
    plainPW << ['yesterday123', 'I really enjoy XKCD.com 936 style Passwords', 'XXYfgLvCehJ6qjflkMRBZ99Jw2I==']
  }

  def 'validate passwords larger then 72 still validate correctly even though silently dropping extra char'() {
    setup:
    def subject = ManagedSubject.build()
    subject.hash = null
    subject.plainPassword = pw
    subject.plainPasswordConfirmation = pw

    expect:
    subject.hash == null

    when:
    cs.generateBCryptHash(subject)

    then:
    subject.hash.size() == 60
    subject.validate()
    cs.verifyBCryptHash(plainPW, subject) == expected

    where:
    pw << [ '1Tix4JWX8OdFgPrf0JBy/RQAE1SjMgkP/yjG6cDFV7fElPgQTe3vuL77w95qwcUvU+Nqh9D89o', 
            '1Tix4JWX8OdFgPrf0JBy/RQAE1SjMgkP/yjG6cDFV7fElPgQTe3vuL77w95qwcUvU+Nqh9D89o',
            '1Tix4JWX8OdFgPrf0JBy/RQAE1SjMgkP/yjG6cDFV7fElPgQTe3vuL77w95qwcUvU+Nqh9D89o']
    plainPW << ['1Tix4JWX8OdFgPrf0JBy/RQAE1SjMgkP/yjG6cDFV7fElPgQTe3vuL77w95qwcUvU+Nqh9D89o', 
                '1Tix4JWX8OdFgPrf0JBy/RQAE1SjMgkP/yjG6cDFV7fElPgQTe3vuL77w95qwcUvU+Nqh9D8',
                '1Tix4JWX8OdFgPrf0JBy/RQAE1SjMgkP/yjG6cDFV7fElPgQTe3vuL77w95qwcUvU+Nqh9D']
    expected << [true, true, false]
  }

  def 'Validate sha512 hash creation for ChallengeResponse'() {
    setup:
    def cr = new ChallengeResponse(challenge:challenge)
    cr.response = response

    expect:
    cr.hash == null
    cr.challenge == challenge
    cr.response == response

    when:
    cs.generateSha512Hash(cr)

    then:
    cr.hash == hash

    where:
    challenge << ["What was your mothers maiden name?",
                  "What is your favourite TV show?",
                  "What is the haxors name who wrote this test?"]
    response << ["My mother's maiden name was jones",
                 "Inspector Gadget! goGadgetgo",
                 "bradleY"]

    // Generated via OpenSSL to test conformance of Java Impl.
    hash << ["03adaadcff390d6d5a035c437af1636c0a376c6d9a67a2faf0c749f169a441d70692572e6b30e8af636cae24274f4c0236ad480ec8687404a93b1f433d13ce04",
             "0fd44bcdae1f30a2e691da0229f5edfa229661f0ffa4ea1d13838b100efc95f0c5f1797fbf9c831cc5068e9df40d033b54146e61870370d6546758ca9c66ae46",
             "0e819f575d8ca7e9b12dec270db4208c0ae20746d647432b2f846aff7ffc559c1029b85b23b7d25fa42a4d39aa3f76f6f9199310472ab1cb28921e3e5347db47"]
  }

  def 'Ensure sha512 hash validation for ChallengeResponse'() {
    setup:
    def cr = new ChallengeResponse(challenge:challenge, hash:hash)
    cr.response = response

    expect:
    cr.hash == hash
    cr.challenge == challenge

    when:
    cs.verifySha512Hash(response, cr)

    then:
    cr.hash == hash

    where:
    challenge << ["What was your mothers maiden name?",
                  "What is your favourite TV show?",
                  "What is the haxors name who wrote this test?"]
    response << ["My mother's maiden name was jones",
                 "Inspector Gadget! goGadgetgo",
                 "bradleY"]

    // Generated via OpenSSL to test conformance of Java Impl.
    hash << ["03adaadcff390d6d5a035c437af1636c0a376c6d9a67a2faf0c749f169a441d70692572e6b30e8af636cae24274f4c0236ad480ec8687404a93b1f433d13ce04",
             "0fd44bcdae1f30a2e691da0229f5edfa229661f0ffa4ea1d13838b100efc95f0c5f1797fbf9c831cc5068e9df40d033b54146e61870370d6546758ca9c66ae46",
             "0e819f575d8ca7e9b12dec270db4208c0ae20746d647432b2f846aff7ffc559c1029b85b23b7d25fa42a4d39aa3f76f6f9199310472ab1cb28921e3e5347db47"]
  }

}
