package aaf.vhr

import grails.test.mixin.*
import grails.buildtestdata.mixin.Build
import spock.lang.*
import grails.plugin.spock.*

import aaf.vhr.ManagedSubject

@TestFor(aaf.vhr.Attribute)
@Build([Attribute])
class AttributeSpec extends UnitSpec {

  def 'ensure name must be unique'() {
    setup:
    def a = Attribute.build()
    def a2 = Attribute.build()

    mockForConstraintsTests(Attribute, [a])

    expect:
    a != a2
    a.validate()
    a2.validate()

    when:
    a.name = a2.name

    then:
    !a.save()
    'unique' == a.errors['name']
  }

  def 'ensure name must not be null or blank'() {
    setup:
    def a = Attribute.build()
    mockForConstraintsTests(Attribute, [a])

    expect:
    a.validate()

    when:
    a.name = val

    then:
    !a.save()
    reason == a.errors['name']

    where:
    val << [null, '']
    reason << ['nullable', 'blank']
  }

  def 'ensure oid must not be null or blank'() {
    setup:
    def a = Attribute.build()
    mockForConstraintsTests(Attribute, [a])

    expect:
    a.validate()

    when:
    a.oid = val

    then:
    !a.save()
    reason == a.errors['oid']

    where:
    val << [null, '']
    reason << ['nullable', 'blank']
  }

  def 'ensure description must not be null or blank'() {
    setup:
    def a = Attribute.build()
    mockForConstraintsTests(Attribute, [a])

    expect:
    a.validate()

    when:
    a.description = val

    then:
    !a.save()
    reason == a.errors['description']

    where:
    val << [null, '']
    reason << ['nullable', 'blank']
  }
}
