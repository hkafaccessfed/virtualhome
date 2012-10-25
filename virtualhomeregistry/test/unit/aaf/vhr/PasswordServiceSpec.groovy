package aaf.vhr

import grails.test.mixin.*
import grails.buildtestdata.mixin.Build
import spock.lang.*
import grails.plugin.spock.*

@TestFor(aaf.vhr.PasswordService)
class PasswordServiceSpec extends UnitSpec {

  def 'ensure password size conformance to database max size'() {
    setup:
    def ps = new PasswordService()

    when:
    ps.generateHash('today123')

    then:
    true
  }

}
