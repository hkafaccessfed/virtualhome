package aaf.vhr

import grails.test.mixin.*
import grails.buildtestdata.mixin.Build
import spock.lang.*
import grails.plugin.spock.*

@TestFor(aaf.vhr.Organization)
@Build([aaf.vhr.Organization])
class OrganizationSpec extends UnitSpec {

  def 'ensure name can not be null or blank'() {
    setup:
    def o = Organization.build()
    o.name = val

    when:
    def result = o.validate()

    then:
    result == expected

    if(!expected)
      reason == o.errors['name']

    where:
    val << [null, '', 'name']
    expected << [false, false, true]
    reason << ['nullable', 'blank', '']
  }

  def 'ensure displayName can not be null or blank'() {
    setup:
    def o = Organization.build()
    o.displayName = val

    when:
    def result = o.validate()

    then:
    result == expected

    if(!expected)
      reason == o.errors['displayName']

    where:
    val << [null, '', 'name']
    expected << [false, false, true]
    reason << ['nullable', 'blank', '']
  }

  def 'ensure description can be null but not blank'() {
    setup:
    def o = Organization.build()
    o.description = val

    when:
    def result = o.validate()

    then:
    result == expected

    if(!expected)
      reason == o.errors['description']

    where:
    val << [null, '', 'name']
    expected << [true, false, true]
    reason << ['', 'blank', '']
  }
}
