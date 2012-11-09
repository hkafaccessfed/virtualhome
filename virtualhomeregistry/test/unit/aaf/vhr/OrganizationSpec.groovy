package aaf.vhr

import grails.test.mixin.*
import grails.buildtestdata.mixin.Build
import spock.lang.*
import grails.plugin.spock.*

@TestFor(aaf.vhr.Organization)
@Build([aaf.vhr.Organization, aaf.vhr.ManagedSubject])
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

  def 'Ensure unlimited and active org can register subjects'() {
    setup:
    def o = Organization.build()
    o.active = true
    o.subjectLimit = 0

    when:
    def result = o.canRegisterSubjects()

    then:
    result
  }

  def 'Ensure unlimited but inactive org can register subjects'() {
    setup:
    def o = Organization.build()
    o.active = false
    o.subjectLimit = 0

    when:
    def result = o.canRegisterSubjects()

    then:
    !result
  }

  def 'Ensure limited, active org that hasnt reached max can register subjects'() {
    setup:
    def o = Organization.build()
    o.active = true
    o.subjectLimit = 100

    (1..99).each {
      def s = ManagedSubject.build()
      o.addToSubjects(s)
    }

    when:
    def result = o.canRegisterSubjects()

    then:
    result
  }

  def 'Ensure limited, active org that has reached max cant register subjects'() {
    setup:
    def o = Organization.build()
    o.active = true
    o.subjectLimit = 100

    (1..100).each {
      def s = ManagedSubject.build()
      o.addToSubjects(s)
    }

    when:
    def result = o.canRegisterSubjects()

    then:
    !result
  }

  def 'Ensure limited, active org some how over max cant register subjects'() {
    setup:
    def o = Organization.build()
    o.active = true
    o.subjectLimit = 100

    (1..101).each {
      def s = ManagedSubject.build()
      o.addToSubjects(s)
    }

    when:
    def result = o.canRegisterSubjects()

    then:
    !result
  }

  def 'Ensure limited, inactive org that hasnt reached max cant register subjects'() {
    setup:
    def o = Organization.build()
    o.active = false
    o.subjectLimit = 100

    (1..99).each {
      def s = ManagedSubject.build()
      o.addToSubjects(s)
    }

    when:
    def result = o.canRegisterSubjects()

    then:
    !result
  }

  def 'Ensure limited, inactive org that has reached max cant register subjects'() {
    setup:
    def o = Organization.build()
    o.active = false
    o.subjectLimit = 100

    (1..100).each {
      def s = ManagedSubject.build()
      o.addToSubjects(s)
    }

    when:
    def result = o.canRegisterSubjects()

    then:
    !result
  }

  def 'Ensure limited, inactive org some how over max cant register subjects'() {
    setup:
    def o = Organization.build()
    o.active = false
    o.subjectLimit = 100

    (1..101).each {
      def s = ManagedSubject.build()
      o.addToSubjects(s)
    }

    when:
    def result = o.canRegisterSubjects()

    then:
    !result
  }

  def 'Ensure active, non workflow Organization is functioning'() {
    setup:
    def o = Organization.build()
    o.active = true
    o.undergoingWorkflow = false

    when:
    def result = o.functioning()

    then:
    result
  }

  def 'Ensure non active, non workflow Organization isnt functioning'() {
    setup:
    def o = Organization.build()
    o.active = false
    o.undergoingWorkflow = false

    when:
    def result = o.functioning()

    then:
    !result
  }

  def 'Ensure non active, workflow Organization isnt functioning'() {
    setup:
    def o = Organization.build()
    o.active = false
    o.undergoingWorkflow = true

    when:
    def result = o.functioning()

    then:
    !result
  }

  def 'Ensure active, workflow Organization isnt functioning'() {
    setup:
    def o = Organization.build()
    o.active = true
    o.undergoingWorkflow = true

    when:
    def result = o.functioning()

    then:
    !result
  }
}
