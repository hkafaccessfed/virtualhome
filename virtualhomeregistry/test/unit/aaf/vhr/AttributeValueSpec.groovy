package aaf.vhr

import grails.test.mixin.*
import grails.buildtestdata.mixin.Build
import spock.lang.*
import grails.plugin.spock.*

import aaf.vhr.ManagedSubject

@TestFor(aaf.vhr.AttributeValue)
@Build([AttributeValue])
class AttributeValueSpec extends UnitSpec {

  def 'ensure value must not be null or blank'() {
    setup:
    def a = AttributeValue.build()
    mockForConstraintsTests(AttributeValue, [a])

    expect:
    a.validate()

    when:
    a.value = val

    then:
    !a.save()
    reason == a.errors['value']

    where:
    val << [null, '']
    reason << ['nullable', 'blank']
  }

  def 'ensure attribute must not be null'() {
    setup:
    def a = AttributeValue.build()
    mockForConstraintsTests(AttributeValue, [a])

    expect:
    a.validate()

    when:
    a.attribute = val

    then:
    !a.save()
    reason == a.errors['attribute']

    where:
    val << [null]
    reason << ['nullable']
  }

  def 'ensure belongsTo must not be null'() {
    setup:
    def a = AttributeValue.build()
    mockForConstraintsTests(AttributeValue, [a])

    expect:
    a.validate()

    when:
    a.subject = val

    then:
    !a.save()
    reason == a.errors['subject']

    where:
    val << [null]
    reason << ['nullable']
  }

}
