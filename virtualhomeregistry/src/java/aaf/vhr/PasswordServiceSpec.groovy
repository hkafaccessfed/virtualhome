package aaf.vhr

import grails.test.mixin.*
import grails.buildtestdata.mixin.Build
import spock.lang.*
import grails.plugin.spock.*

import aaf.vhr.ManagedSubject

@TestFor(aaf.vhr.PasswordService)
@Build([ManagedSubject])
class PasswordServiceSpec extends UnitSpec {

  def ps

  def setup() {
    ps = new PasswordService(grailsApplication: grailsApplication)
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
    ps.generateHash(subject)

    then:
    subject.hash.size() == 60
    subject.validate()
    ps.verify(pw, subject)

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
    ps.generateHash(subject)

    then:
    subject.hash.size() == 60
    subject.validate()
    !ps.verify(plainPW, subject)

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
    ps.generateHash(subject)

    then:
    subject.hash.size() == 60
    subject.validate()
    ps.verify(plainPW, subject) == expected

    where:
    pw << [ '1Tix4JWX8OdFgPrf0JBy/RQAE1SjMgkP/yjG6cDFV7fElPgQTe3vuL77w95qwcUvU+Nqh9D89o', 
            '1Tix4JWX8OdFgPrf0JBy/RQAE1SjMgkP/yjG6cDFV7fElPgQTe3vuL77w95qwcUvU+Nqh9D89o',
            '1Tix4JWX8OdFgPrf0JBy/RQAE1SjMgkP/yjG6cDFV7fElPgQTe3vuL77w95qwcUvU+Nqh9D89o']
    plainPW << ['1Tix4JWX8OdFgPrf0JBy/RQAE1SjMgkP/yjG6cDFV7fElPgQTe3vuL77w95qwcUvU+Nqh9D89o', 
                '1Tix4JWX8OdFgPrf0JBy/RQAE1SjMgkP/yjG6cDFV7fElPgQTe3vuL77w95qwcUvU+Nqh9D8',
                '1Tix4JWX8OdFgPrf0JBy/RQAE1SjMgkP/yjG6cDFV7fElPgQTe3vuL77w95qwcUvU+Nqh9D']
    expected << [true, true, false]
  }

}
