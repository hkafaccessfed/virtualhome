package aaf.vhr

import grails.test.mixin.*
import grails.plugin.spock.*
import grails.buildtestdata.mixin.Build

import spock.lang.*

import test.shared.ShiroEnvironment

@TestFor(aaf.vhr.ManagedSubjectController)
@Build([ManagedSubject, aaf.base.identity.Subject])
class ManagedSubjectControllerSpec  extends spock.lang.Specification {
  
  @Shared def shiroEnvironment = new ShiroEnvironment()

  aaf.base.identity.Subject subject
  org.apache.shiro.subject.Subject shiroSubject
  
  def cleanupSpec() { 
    shiroEnvironment.tearDownShiro() 
  }

  def setup() {
    subject = aaf.base.identity.Subject.build()

    shiroSubject = Mock(org.apache.shiro.subject.Subject)
    shiroSubject.id >> subject.id
    shiroSubject.principal >> subject.principal
    shiroSubject.isAuthenticated() >> true
    shiroEnvironment.setSubject(shiroSubject)
    
    controller.metaClass.getSubject = { subject }
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
    def result = controller.validManagedSubject()

    then:
    !result
    response.status == 302

    response.redirectedUrl == "/managedSubject/list"

    flash.type == 'info'
    flash.message == 'controllers.aaf.vhr.managedsubject.no.id'
  }

  def 'ensure redirect to list if no valid instance found by beforeInterceptor'() {
    when:
    params.id = 1
    def result = controller.validManagedSubject()

    then:
    !result
    response.status == 302

    response.redirectedUrl== "/managedSubject/list"

    flash.type == 'info'
    flash.message == 'controllers.aaf.vhr.managedsubject.notfound'
  }

  def 'ensure redirect to list if no groupID presented to validGroup'() {
    when:
    params.group = [:]
    def result = controller.validGroup()

    then:
    !result
    response.status == 302

    response.redirectedUrl == "/managedSubject/list"

    flash.type == 'info'
    flash.message == 'controllers.aaf.vhr.managedsubject.group.no.id'
  }

  def 'ensure redirect to list if no valid instance found by validGroup'() {
    when:
    params.group = [id:1]
    def result = controller.validGroup()

    then:
    !result
    response.status == 302

    response.redirectedUrl== "/managedSubject/list"

    flash.type == 'info'
    flash.message == 'controllers.aaf.vhr.managedsubject.group.notfound'
  }

  def 'ensure true if no valid group found by validGroup'() {
    setup:
    def group = Group.build()

    when:
    params.group = [id:group.id]
    def result = controller.validGroup()

    then:
    result
  }

  def 'ensure correct output from list'() {
    setup:
    (1..10).each { ManagedSubject.build() }

    when:
    params.max = max
    def model = controller.list()

    then:
    ManagedSubject.count() == total
    model.managedSubjectInstanceList.size() == expectedResult

    where:
    max | total | expectedResult
    0 | 10 | 10
    5 | 10 | 5
  }

  def 'ensure correct output from show'() {
    setup:
    def managedSubjectTestInstance = ManagedSubject.build()

    when:
    params.id = managedSubjectTestInstance.id
    def model = controller.show()

    then:
    model.managedSubjectInstance == managedSubjectTestInstance
  }

  def 'ensure correct output from create when valid permission'() {
    setup:
    def group = Group.build()
    shiroSubject.isPermitted("app:manage:group:${group.id}:managedsubject:create") >> true

    when:
    params.group = [id:group.id]
    def model = controller.create()

    then:
    model.managedSubjectInstance.instanceOf(ManagedSubject)
  }

  def 'ensure correct output from create when invalid permission'() {
    setup:
    def group = Group.build()
    shiroSubject.isPermitted("app:manage:group:${group.id + 1}:managedsubject:create") >> true

    when:
    params.group = [id:group.id]
    def model = controller.create()

    then:
    model == null
    response.status == 403
  }

  def 'ensure correct output from save when invalid permission'() {
    setup:
    def group = Group.build()
    shiroSubject.isPermitted("app:manage:group:${group.id + 1}:managedsubject:create") >> true

    when:
    params.group = [id:group.id]
    def model = controller.save()

    then:
    model == null
    response.status == 403
  }

  def 'ensure correct output from save with invalid data and when valid permission'() {
    setup:
    def sharedTokenService = Mock(aaf.vhr.SharedTokenService)
    def group = Group.build()
    shiroSubject.isPermitted("app:manage:group:${group.id}:managedsubject:create") >> true

    def managedSubjectTestInstance = ManagedSubject.build(group:group, organization:group.organization)
    managedSubjectTestInstance.properties.each {
      if(it.value) {
        if(grailsApplication.isDomainClass(it.value.getClass()))
          params."${it.key}" = [id:"${it.value.id}"]
        else
          params."${it.key}" = "${it.value}"
      }
    }
    managedSubjectTestInstance.delete()

    ManagedSubject.metaClass.save { null }

    controller.sharedTokenService = sharedTokenService
    
    when:
    controller.save()

    then:
    1 * sharedTokenService.generate(_ as ManagedSubject)

    ManagedSubject.count() == 0
    flash.type == 'error'
    flash.message == 'controllers.aaf.vhr.managedsubject.validate.failed'

    model.managedSubjectInstance.properties.each {
      it.value == managedSubjectTestInstance.getProperty(it.key)
    }
  }

  def 'ensure correct output from save with valid data and when valid permission'() {
    setup:
    def sharedTokenService = Mock(aaf.vhr.SharedTokenService)
    def managedSubjectService = Mock(aaf.vhr.ManagedSubjectService)
    def group = Group.build()
    shiroSubject.isPermitted("app:manage:group:${group.id}:managedsubject:create") >> true

    def managedSubjectTestInstance = ManagedSubject.build(group:group, organization:group.organization)
    managedSubjectTestInstance.properties.each {
      if(it.value) {
        if(grailsApplication.isDomainClass(it.value.getClass()))
          params."${it.key}" = [id:"${it.value.id}"]
        else
          params."${it.key}" = "${it.value}"
      }
    }
    managedSubjectTestInstance.delete()

    controller.sharedTokenService = sharedTokenService
    controller.managedSubjectService = managedSubjectService

    expect:
    ManagedSubject.count() == 0

    when:
    controller.save()

    then:
    1 * sharedTokenService.generate(_ as ManagedSubject) >> {ManagedSubject subject -> subject.sharedToken = '1234'}
    1 * managedSubjectService.register(_ as ManagedSubject)>> {ManagedSubject subject -> subject.save()}
    ManagedSubject.count() == 1
    flash.type == 'success'
    flash.message == 'controllers.aaf.vhr.managedsubject.save.success'

    def savedManagedSubjectTestInstance = ManagedSubject.first()
    savedManagedSubjectTestInstance.properties.each {
      it.value == managedSubjectTestInstance.getProperty(it.key)
    }
  }

  def 'ensure correct output from edit when invalid permission'() {
    setup:
    def group = Group.build()
    def managedSubjectTestInstance = ManagedSubject.build(group:group, organization:group.organization)
    shiroSubject.isPermitted("app:manage:group:${group.id + 1}:managedsubject:edit") >> true

    when:
    params.id = managedSubjectTestInstance.id
    def model = controller.edit()

    then:
    model == null
    response.status == 403
  }

  def 'ensure correct output from edit when valid permission'() {
    setup:
    def group = Group.build()
    def managedSubjectTestInstance = ManagedSubject.build(group:group, organization:group.organization)
    shiroSubject.isPermitted("app:manage:group:${group.id}:managedsubject:edit") >> true

    when:
    params.id = managedSubjectTestInstance.id
    def model = controller.edit()

    then:
    model.managedSubjectInstance == managedSubjectTestInstance
  }

  def 'ensure correct output from update when invalid permission'() {
    setup:
    def group = Group.build()
    def managedSubjectTestInstance = ManagedSubject.build(group:group, organization:group.organization)
    shiroSubject.isPermitted("app:manage:group:${group.id + 1}:managedsubject:edit") >> true

    when:
    params.id = managedSubjectTestInstance.id
    def model = controller.update()

    then:
    model == null
    response.status == 403
  }

  def 'ensure correct output from update with null version but valid permission'() {
    setup:
    def group = Group.build()
    def managedSubjectTestInstance = ManagedSubject.build(group:group, organization:group.organization)
    shiroSubject.isPermitted("app:manage:group:${group.id}:managedsubject:edit") >> true
    
    expect:
    ManagedSubject.count() == 1

    when:
    params.id = managedSubjectTestInstance.id
    params.version = null
    controller.update()

    then:
    ManagedSubject.count() == 1
    flash.type == 'error'
    flash.message == 'controllers.aaf.vhr.managedsubject.update.noversion'
  }

  def 'ensure correct output from update with invalid data and when valid permission'() {
    setup:
    def group = Group.build()
    def managedSubjectTestInstance = ManagedSubject.build(group:group, organization:group.organization)
    shiroSubject.isPermitted("app:manage:group:${group.id}:managedsubject:edit") >> true
    managedSubjectTestInstance.getVersion() >> 20
    
    managedSubjectTestInstance.properties.each {
      if(it.value) {
        if(grailsApplication.isDomainClass(it.value.getClass()))
          params."${it.key}" = [id:"${it.value.id}"]
        else
          params."${it.key}" = "${it.value}"
      }
    }
    ManagedSubject.metaClass.save { null }
    
    expect:
    ManagedSubject.count() == 1

    when:
    params.id = managedSubjectTestInstance.id
    params.version = 1
    controller.update()

    then:
    ManagedSubject.count() == 1
    flash.type == 'error'
    flash.message == 'controllers.aaf.vhr.managedsubject.update.failed'

    model.managedSubjectInstance.properties.each {
      it.value == managedSubjectTestInstance.getProperty(it.key)
    }
  }

  def 'ensure correct output from update with valid data and when valid permission'() {
    setup:
    def group = Group.build()
    def managedSubjectTestInstance = ManagedSubject.build(group:group, organization:group.organization)
    shiroSubject.isPermitted("app:manage:group:${group.id}:managedsubject:edit") >> true
    
    managedSubjectTestInstance.properties.each {
      if(it.value) {
        if(grailsApplication.isDomainClass(it.value.getClass()))
          params."${it.key}" = [id:"${it.value.id}"]
        else
          params."${it.key}" = "${it.value}"
      }
    }

    expect:
    ManagedSubject.count() == 1

    when:
    params.id = managedSubjectTestInstance.id
    params.version = 0
    controller.update()

    then:
    ManagedSubject.count() == 1
    flash.type == 'success'
    flash.message == 'controllers.aaf.vhr.managedsubject.update.success'

    def savedManagedSubjectTestInstance = ManagedSubject.first()
    savedManagedSubjectTestInstance == managedSubjectTestInstance

    savedManagedSubjectTestInstance.properties.each {
      it.value == managedSubjectTestInstance.getProperty(it.key)
    }
  }

  def 'ensure correct output from delete when invalid permission'() {
    setup:
    def group = Group.build()
    def managedSubjectTestInstance = ManagedSubject.build(group:group, organization:group.organization)
    shiroSubject.isPermitted("app:manage:group:${group.id + 1}:managedsubject:delete") >> true

    when:
    params.id = managedSubjectTestInstance.id
    def model = controller.delete()

    then:
    model == null
    response.status == 403
  }

  def 'ensure correct output from delete when valid permission'() {
    setup:
    def group = Group.build()
    def managedSubjectTestInstance = ManagedSubject.build(group:group, organization:group.organization)
    shiroSubject.isPermitted("app:manage:group:${group.id}:managedsubject:delete") >> true

    expect:
    ManagedSubject.count() == 1

    when:
    params.id = managedSubjectTestInstance.id
    def model = controller.delete()

    then:
    ManagedSubject.count() == 0

    response.redirectedUrl == "/managedSubject/list"

    flash.type == 'success'
    flash.message == 'controllers.aaf.vhr.managedsubject.delete.success'
  }

  def 'ensure correct output from delete when integrity violation'() {
    setup:
    def group = Group.build()
    def managedSubjectTestInstance = ManagedSubject.build(group:group, organization:group.organization)
    shiroSubject.isPermitted("app:manage:group:${group.id}:managedsubject:delete") >> true

    ManagedSubject.metaClass.delete { throw new org.springframework.dao.DataIntegrityViolationException("Thrown from test case") }

    expect:
    ManagedSubject.count() == 1

    when:
    params.id = managedSubjectTestInstance.id
    def model = controller.delete()

    then:
    ManagedSubject.count() == 1

    response.redirectedUrl == "/managedSubject/show/${managedSubjectTestInstance.id}"

    flash.type == 'error'
    flash.message == 'controllers.aaf.vhr.managedsubject.delete.failure'
  }

  def 'ensure correct output from resend when invalid permission'() {
    setup:
    def group = Group.build()
    def managedSubjectTestInstance = ManagedSubject.build(group:group, organization:group.organization)
    shiroSubject.isPermitted("app:manage:group:${group.id + 1}:managedsubject:edit") >> true

    when:
    params.id = managedSubjectTestInstance.id
    def model = controller.resend()

    then:
    model == null
    response.status == 403
  }

  def 'ensure correct output from resend when invalid permission'() {
    setup:
    def group = Group.build()
    def managedSubjectTestInstance = ManagedSubject.build(group:group, organization:group.organization)
    shiroSubject.isPermitted("app:manage:group:${group.id + 1}:managedsubject:edit") >> true

    when:
    params.id = managedSubjectTestInstance.id
    def model = controller.resend()

    then:
    model == null
    response.status == 403
  }

  def 'ensure correct output from resend with valid data and when valid permission'() {
    setup:
    def managedSubjectService = Mock(aaf.vhr.ManagedSubjectService)
    def group = Group.build()
    def managedSubjectTestInstance = ManagedSubject.build(group:group, organization:group.organization)
    shiroSubject.isPermitted("app:manage:group:${group.id}:managedsubject:edit") >> true
    
    controller.managedSubjectService = managedSubjectService

    expect:
    ManagedSubject.count() == 1

    when:
    params.id = managedSubjectTestInstance.id
    controller.resend()

    then:
    ManagedSubject.count() == 1
    flash.type == 'success'
    flash.message == 'controllers.aaf.vhr.managedsubject.resend.success'

    1 * managedSubjectService.sendConfirmation(_ as ManagedSubject)
  }
}
