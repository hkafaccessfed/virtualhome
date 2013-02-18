package aaf.vhr

import grails.test.mixin.*
import grails.buildtestdata.mixin.Build
import spock.lang.*
import grails.plugin.spock.*

@TestFor(aaf.vhr.Group)
@Build([aaf.vhr.Group, aaf.vhr.ManagedSubject, aaf.vhr.Organization])
class GroupSpec extends UnitSpec {

  def 'name is required to be valid'() {
    setup:
    def g = Group.build()
    mockForConstraintsTests(Group, [g])

    when:
    g.name = val
    def result = g.validate()

    then:
    result == expected

    if (!expected)
      reason == g.errors['name']

    where:
    val << [null, '', 'name']
    expected << [false, false, true]
    reason << ['null', 'blank', '']
  }

  def 'description is required to be valid'() {
    setup:
    def g = Group.build()
    mockForConstraintsTests(Group, [g])

    when:
    g.description = val
    def result = g.validate()

    then:
    result == expected

    if (!expected)
      reason == g.errors['description']

    where:
    val << [null, '', 'name']
    expected << [false, false, true]
    reason << ['null', 'blank', '']
  }

  def 'functioning when active and Organization functioning' () {
    setup:
    def g = Group.build()

    when:
    g.active = true
    g.organization.active = true

    then:
    g.functioning()
  }

  def 'not functioning when inactive and Organization functioning' () {
    setup:
    def g = Group.build()

    when:
    g.active = false
    g.organization.active = true

    then:
    !g.functioning()
  }

  def 'not functioning when inactive and Organization not functioning' () {
    setup:
    def g = Group.build()

    when:
    g.active = false
    g.organization.active = false

    then:
    !g.functioning()
  }

  def 'not functioning when active but Organization not functioning' () {
    setup:
    def g = Group.build()

    when:
    g.active = true
    g.organization.active = false

    then:
    !g.functioning()
  }

  def 'not functioning when blocked' () {
    setup:
    def g = Group.build()
    g.organization.active = true

    expect:
    g.functioning()

    when:
    g.blocked = true

    then:
    !g.functioning()
  }

  def 'not functioning when archived' () {
    setup:
    def g = Group.build()
    g.organization.active = true

    expect:
    g.functioning()

    when:
    g.archived = true

    then:
    !g.functioning()
  }

}
