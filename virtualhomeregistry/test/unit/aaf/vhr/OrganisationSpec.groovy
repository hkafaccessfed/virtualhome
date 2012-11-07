package aaf.vhr

import grails.test.mixin.*
import grails.buildtestdata.mixin.Build
import spock.lang.*
import grails.plugin.spock.*

@TestFor(aaf.vhr.Organisation)
@Build([aaf.vhr.Organisation])
class OrganisationSpec extends UnitSpec {

  def 'ensure name can not be null or blank'() {
    setup:
    def o = Organisation.build()
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
    def o = Organisation.build()
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
    def o = Organisation.build()
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

  def 'ensure url can not be null or blank and must be url formatted'() {
    setup:
    def o = Organisation.build()
    o.url = val

    when:
    def result = o.validate()

    then:
    result == expected

    if(!expected)
      reason == o.errors['url']

    where:
    val << [null, '', 'name', 'http://url.com']
    expected << [false, false, false, true]
    reason << ['nullable', 'blank', 'url', '']
  }

  def 'ensure frURL can not be null or blank and must be url formatted'() {
    setup:
    def o = Organisation.build()
    o.frURL = val

    when:
    def result = o.validate()

    then:
    result == expected

    if(!expected)
      reason == o.errors['frURL']

    where:
    val << [null, '', 'name', 'http://url.com']
    expected << [false, false, false, true]
    reason << ['nullable', 'blank', 'url', '']
  }
}
