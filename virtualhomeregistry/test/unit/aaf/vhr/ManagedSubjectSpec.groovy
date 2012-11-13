package aaf.vhr

import grails.test.mixin.*
import grails.buildtestdata.mixin.Build
import spock.lang.*
import grails.plugin.spock.*

import aaf.vhr.ManagedSubject

@TestFor(aaf.vhr.ManagedSubject)
@Build([ManagedSubject, Organization, Group, Attribute, AttributeValue, ChallengeResponse])
class ManagedSubjectSpec extends UnitSpec {

  def 'ensure login can be null'() {
    setup:
    def s = ManagedSubject.build()
    mockForConstraintsTests(ManagedSubject, [s])

    expect:
    s.validate()
    s.login != null

    when:
    s.login = null

    then:
    s.save()
  }

  def 'ensure login must not be blank'() {
    setup:
    def s = ManagedSubject.build()
    mockForConstraintsTests(ManagedSubject, [s])

    expect:
    s.validate()

    when:
    s.login = val

    then:
    !s.save()
    reason == s.errors['login']

    where:
    val << ['']
    reason << ['blank']
  }

  def 'ensure login must be unique'() {
    setup:
    def s = ManagedSubject.build()
    def s2 = ManagedSubject.build()
    mockForConstraintsTests(ManagedSubject, [s])

    expect:
    s.validate()
    s2.validate()

    when:
    s.login = s2.login

    then:
    !s.save()
    'unique' == s.errors['login']
  }

  def 'ensure subject stores attribute values'() {
    setup:
    def s = ManagedSubject.build()
    def a = Attribute.build()

    expect:
    s.save()

    when:
    def av = AttributeValue.build(attribute:a)
    s.addToPii(av)

    then:
    s.save()
    s.pii.size() == 1
    s.pii[0].subject == s
  }

  def 'ensure subject stores multiple attribute values'() {
    setup:
    def s = ManagedSubject.build()
    def a = Attribute.build(name:'a1')
    def a2 = Attribute.build(name:'a2')
    def a3 = Attribute.build(name:'a3')

    expect:
    s.save()

    when:
    def av = AttributeValue.build(attribute:a)
    def av2 = AttributeValue.build(attribute:a2)
    def av3 = AttributeValue.build(attribute:a3)
    s.addToPii(av)
    s.addToPii(av2)
    s.addToPii(av3)

    then:
    s.save()
    s.pii.size() == 3
  }

  def 'ensure subject stores challenge responses'() {
    setup:
    def s = ManagedSubject.build()
    def cr = ChallengeResponse.build()

    expect:
    s.save()

    when:
    s.addToChallengeResponse(cr)

    then:
    s.save()
    s.challengeResponse.size() == 1
    s.challengeResponse[0].subject == s
  }

  def 'ensure subject stores multiple challenge responses'() {
    setup:
    def s = ManagedSubject.build()
    def cr = ChallengeResponse.build()
    def cr2 = ChallengeResponse.build()
    def cr3 = ChallengeResponse.build()

    expect:
    s.save()

    when:
    s.addToChallengeResponse(cr)
    s.addToChallengeResponse(cr2)
    s.addToChallengeResponse(cr3)

    then:
    s.save()
    s.challengeResponse.size() == 3
    s.challengeResponse[2].subject == s
  }

  def 'ensure email must not be null or blank and be an email address'() {
    setup:
    def s = ManagedSubject.build()
    mockForConstraintsTests(ManagedSubject, [s])

    expect:
    s.validate()

    when:
    s.email = val
    def result = s.validate()

    then:
    result == expectedResult

    if (!expectedResult)
      reason == s.errors['email']

    where:
    val << [null, '', 'testuser', 'testuser@domain.com']
    reason << ['nullable', 'blank', 'email', '']
    expectedResult << [false, false, false, true]
  }

  def 'ensure cn must not be null or blank and either singular or first<space>last name formatted'() {
    setup:
    def s = ManagedSubject.build()
    mockForConstraintsTests(ManagedSubject, [s])

    expect:
    s.validate()

    when:
    s.cn = val
    def result = s.validate()

    then:
    result == expectedResult

    if (!expectedResult)
      reason == s.errors['cn']

    where:
    val << [null, '', 'Testuser', 'Test User', 'Mr Test User']
    reason << ['nullable', 'blank', '', '', 'cn']
    expectedResult << [false, false, true, true, false]
  }

  def 'ensure sharedtoken must not be null or blank'() {
    setup:
    def s = ManagedSubject.build()
    mockForConstraintsTests(ManagedSubject, [s])

    expect:
    s.validate()

    when:
    s.sharedToken = val
    def result = s.validate()

    then:
    result == expectedResult

    if (!expectedResult)
      reason == s.errors['sharedToken']

    where:
    val << [null, '', 'dfasf$@5asf#$',]
    reason << ['nullable', 'blank', '']
    expectedResult << [false, false, true]
  }

  def 'ensure shared token must be unique'() {
    setup:
    def s = ManagedSubject.build()
    def s2 = ManagedSubject.build()
    mockForConstraintsTests(ManagedSubject, [s])

    expect:
    s.validate()
    s2.validate()

    when:
    s.sharedToken = s2.sharedToken

    then:
    !s.save()
    'unique' == s.errors['sharedToken']
  }

  def 'ensure mobileNumber can be null not be blank'() {
    setup:
    def s = ManagedSubject.build()
    mockForConstraintsTests(ManagedSubject, [s])

    expect:
    s.validate()

    when:
    s.mobileNumber = val

    then:
    expected == s.validate()
    if(!expected)
     reason == s.errors['mobileNumber']

    where:
    val << [null, '', '0411222333']
    reason << ['', 'blank', '']
    expected << [true, false, true]
  }

  def 'ensure active is true by default'() {
    when:
    def s = ManagedSubject.build()

    then:
    s.active
  }

  def 'ensure active is true by default'() {
    when:
    def s = ManagedSubject.build()

    then:
    s.active
  }

  def 'ensure no sponsored org is valid'() {
    setup:
    def s = ManagedSubject.build()

    when:
    s.organization = null

    then:
    s.validate()
  }

  def 'ensure no sponsored group is valid'() {
    setup:
    def s = ManagedSubject.build()

    when:
    s.group = null

    then:
    s.validate()
  }

  def 'ensure no sponsored org or group is valid'() {
    setup:
    def s = ManagedSubject.build()

    when:
    s.organization = null
    s.group = null

    then:
    s.validate()
  }

  def 'ensure functioning when active and sponsored'() {
    setup:
    def s = ManagedSubject.build()

    when:
    s.organization = Organization.build(active:true)
    s.group = Group.build(organization:s.organization, active:true)

    then:
    s.functioning()
  }

  def 'ensure not functioning when active and sponsor org is not functioning'() {
    setup:
    def s = ManagedSubject.build()

    when:
    s.organization = Organization.build(active:false)
    s.group = Group.build(organization:s.organization, active:true)

    then:
    !s.functioning()
  }

  def 'ensure not functioning when active and sponsor group is not functioning'() {
    setup:
    def s = ManagedSubject.build()

    when:
    s.organization = Organization.build(active:true)
    s.group = Group.build(organization:s.organization, active:false)

    then:
    !s.functioning()
  }

  def 'ensure not functioning when inactive and sponsored'() {
    setup:
    def s = ManagedSubject.build()

    when:
    s.active = false
    s.organization = Organization.build()
    s.group = Group.build(organization:s.organization)

    then:
    !s.functioning()
  }

  def 'ensure not functioning when active but not sponsor org'() {
    setup:
    def s = ManagedSubject.build()

    when:
    s.organization = null
    s.group = Group.build()

    then:
    !s.functioning()
  }

  def 'ensure not functioning when active but not sponsor group'() {
    setup:
    def s = ManagedSubject.build()

    when:
    s.organization = Organization.build()
    s.group = null

    then:
    !s.functioning()
  }

  def 'ensure not functioning when active but not sponsored'() {
    setup:
    def s = ManagedSubject.build()

    when:
    s.organization = null
    s.group = null

    then:
    !s.functioning()
  }

}
