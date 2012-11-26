package aaf.vhr

import grails.test.mixin.*
import grails.plugin.spock.*
import grails.buildtestdata.mixin.Build

import spock.lang.*

import test.shared.ShiroEnvironment

@TestFor(aaf.vhr.GroupController)
@Build([Group])
class GroupControllerSpec  extends spock.lang.Specification {

  def subject
  @Shared def shiroEnvironment = new ShiroEnvironment()
  
  def cleanupSpec() { 
    shiroEnvironment.tearDownShiro() 
  }

  def setup() {
    subject = Mock(org.apache.shiro.subject.Subject)

    subject.isAuthenticated() >> true
    shiroEnvironment.setSubject(subject)
    
    controller.metaClass.getSubject = { [id:1, principal:'http://test.com!http://sp.test.com!1234'] }
  }

  def 'ensure beforeInterceptor only excludes list, create, save'() {
    when:
    controller

    then:
    controller.beforeInterceptor.except.size() == 3
    controller.beforeInterceptor.except.containsAll(['list', 'create', 'save'])
  }

  def 'ensure redirect to list if no ID presented to beforeInterceptor'() {
    when:
    def result = controller.validGroup()

    then:
    !result
    response.status == 302

    response.redirectedUrl == "/group/list"

    flash.type == 'info'
    flash.message == 'controllers.aaf.vhr.group.no.id'
  }

  def 'ensure redirect to list if no valid instance found by beforeInterceptor'() {
    when:
    params.id = 1
    def result = controller.validGroup()

    then:
    !result
    response.status == 302

    response.redirectedUrl== "/group/list"

    flash.type == 'info'
    flash.message == 'controllers.aaf.vhr.group.notfound'
  }

  def 'ensure correct output from list'() {
    setup:
    (1..10).each { Group.build() }

    when:
    params.max = max
    def model = controller.list()

    then:
    Group.count() == total
    model.groupInstanceList.size() == expectedResult

    where:
    max | total | expectedResult
    0 | 10 | 10
    5 | 10 | 5
  }

  def 'ensure correct output from show'() {
    setup:
    def groupTestInstance = Group.build()

    when:
    params.id = groupTestInstance.id
    def model = controller.show()

    then:
    model.groupInstance == groupTestInstance
  }

  def 'ensure correct output from create when valid permission'() {
    setup:
    subject.isPermitted("app:manage:group:create") >> true

    when:
    def model = controller.create()

    then:
    model.groupInstance.instanceOf(Group)
  }

  def 'ensure correct output from create when invalid permission'() {
    setup:
    subject.isPermitted("app:manage:group:create") >> false

    when:
    def model = controller.create()

    then:
    model == null
    response.status == 403
  }

  def 'ensure correct output from save when invalid permission'() {
    setup:
    subject.isPermitted("app:manage:group:create") >> false

    when:
    def model = controller.save()

    then:
    model == null
    response.status == 403
  }

  def 'ensure correct output from save with invalid data and when valid permission'() {
    setup:
    subject.isPermitted("app:manage:group:create") >> true

    def groupTestInstance = Group.build()
    groupTestInstance.properties.each {
      if(it.value) {
        if(it.value.hasProperty('id'))
          params."${it.key}.id" = "${it.value.id}"
        else
          params."${it.key}" = "${it.value}"
      }
    }
    groupTestInstance.delete()

    Group.metaClass.save { null }
    
    when:
    controller.save()

    then:
    Group.count() == 0
    flash.type == 'error'
    flash.message == 'controllers.aaf.vhr.group.save.failed'

    model.groupInstance.properties.each {
      it.value == groupTestInstance.getProperty(it.key)
    }
  }

  def 'ensure correct output from save with valid data and when valid permission'() {
    setup:
    subject.isPermitted("app:manage:group:create") >> true

    def groupTestInstance = Group.build()
    groupTestInstance.properties.each {
      if(it.value) {
        if(it.value.hasProperty('id'))
          params."${it.key}.id" = "${it.value.id}"
        else
          params."${it.key}" = "${it.value}"
      }
    }
    groupTestInstance.delete()

    expect:
    Group.count() == 0

    when:
    controller.save()

    then:
    Group.count() == 1
    flash.type == 'success'
    flash.message == 'controllers.aaf.vhr.group.save.success'

    def savedGroupTestInstance = Group.first()
    savedGroupTestInstance.properties.each {
      it.value == groupTestInstance.getProperty(it.key)
    }
  }

  def 'ensure correct output from edit when invalid permission'() {
    setup:
    def groupTestInstance = Group.build()
    subject.isPermitted("app:manage:group:${groupTestInstance.id}:edit") >> false

    when:
    params.id = groupTestInstance.id
    def model = controller.edit()

    then:
    model == null
    response.status == 403
  }

  def 'ensure correct output from edit when valid permission'() {
    setup:
    def groupTestInstance = Group.build()
    subject.isPermitted("app:manage:group:${groupTestInstance.id}:edit") >> true

    when:
    params.id = groupTestInstance.id
    def model = controller.edit()

    then:
    model.groupInstance == groupTestInstance
  }

  def 'ensure correct output from update when invalid permission'() {
    setup:
    def groupTestInstance = Group.build()
    subject.isPermitted("app:manage:group:${groupTestInstance}.id}:edit") >> false

    when:
    def model = controller.update()

    then:
    model == null
    response.status == 403
  }

  def 'ensure correct output from update with null version but valid permission'() {
    setup:
    def groupTestInstance = Group.build()
    subject.isPermitted("app:manage:group:${groupTestInstance.id}:edit") >> true
    
    expect:
    Group.count() == 1

    when:
    params.id = groupTestInstance.id
    params.version = null
    controller.update()

    then:
    Group.count() == 1
    flash.type == 'error'
    flash.message == 'controllers.aaf.vhr.group.update.noversion'
  }

  def 'ensure correct output from update with invalid data and when valid permission'() {
    setup:
    def groupTestInstance = Group.build()
    subject.isPermitted("app:manage:group:${groupTestInstance.id}:edit") >> true
    groupTestInstance.getVersion() >> 20
    
    groupTestInstance.properties.each {
      if(it.value) {
        if(it.value.hasProperty('id'))
          params."${it.key}.id" = "${it.value.id}"
        else
          params."${it.key}" = "${it.value}"
      }
    }
    Group.metaClass.save { null }
    
    expect:
    Group.count() == 1

    when:
    params.id = groupTestInstance.id
    params.version = 1
    controller.update()

    then:
    Group.count() == 1
    flash.type == 'error'
    flash.message == 'controllers.aaf.vhr.group.update.failed'

    model.groupInstance.properties.each {
      it.value == groupTestInstance.getProperty(it.key)
    }
  }

  def 'ensure correct output from update with valid data and when valid permission'() {
    setup:
    def groupTestInstance = Group.build()
    subject.isPermitted("app:manage:group:${groupTestInstance.id}:edit") >> true
    
    groupTestInstance.properties.each {
      if(it.value) {
        if(it.value.hasProperty('id'))
          params."${it.key}.id" = "${it.value.id}"
        else
          params."${it.key}" = "${it.value}"
      }
    }

    expect:
    Group.count() == 1

    when:
    params.id = groupTestInstance.id
    params.version = 0
    controller.update()

    then:
    Group.count() == 1
    flash.type == 'success'
    flash.message == 'controllers.aaf.vhr.group.update.success'

    def savedGroupTestInstance = Group.first()
    savedGroupTestInstance == groupTestInstance

    savedGroupTestInstance.properties.each {
      it.value == groupTestInstance.getProperty(it.key)
    }
  }

  def 'ensure correct output from delete when invalid permission'() {
    setup:
    def groupTestInstance = Group.build()
    subject.isPermitted("app:manage:group:${groupTestInstance.id}:delete") >> false

    when:
    params.id = groupTestInstance.id
    def model = controller.delete()

    then:
    model == null
    response.status == 403
  }

  def 'ensure correct output from delete when valid permission'() {
    setup:
    def groupTestInstance = Group.build()
    subject.isPermitted("app:manage:group:${groupTestInstance.id}:delete") >> true

    expect:
    Group.count() == 1

    when:
    params.id = groupTestInstance.id
    def model = controller.delete()

    then:
    Group.count() == 0

    response.redirectedUrl == "/group/list"

    flash.type == 'success'
    flash.message == 'controllers.aaf.vhr.group.delete.success'
  }

  def 'ensure correct output from delete when integrity violation'() {
    setup:
    def groupTestInstance = Group.build()
    subject.isPermitted("app:manage:group:${groupTestInstance.id}:delete") >> true

    Group.metaClass.delete { throw new org.springframework.dao.DataIntegrityViolationException("Thrown from test case") }

    expect:
    Group.count() == 1

    when:
    params.id = groupTestInstance.id
    def model = controller.delete()

    then:
    Group.count() == 1

    response.redirectedUrl == "/group/show/${groupTestInstance.id}"

    flash.type == 'error'
    flash.message == 'controllers.aaf.vhr.group.delete.failure'
  }
}
