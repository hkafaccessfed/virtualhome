package aaf.vhr

import grails.test.mixin.*
import grails.buildtestdata.mixin.Build
import spock.lang.*
import grails.plugin.spock.*

import aaf.vhr.ManagedSubject

@TestFor(aaf.vhr.ManagedSubject)
@Build([ManagedSubject, Attribute, AttributeValue])
class ManagedSubjectSpec extends UnitSpec {

  def 'ensure login can be null'() {
    setup:
    def s = ManagedSubject.build(login:'login')
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
    def s = ManagedSubject.build(login:'login123')
    def s2 = ManagedSubject.build(login:'login456')
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

}
