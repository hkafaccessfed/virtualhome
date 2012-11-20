<%=packageName ? "package ${packageName}\n\n" : ''%>import org.springframework.dao.DataIntegrityViolationException

class ${className}Controller {

  static defaultAction = "list"
  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def list() {
    [${propertyName}List: ${className}.list(params), ${propertyName}Total: ${className}.count()]
  }

  def create() {
    [${propertyName}: new ${className}(params)]
  }

  def save() {
    def ${propertyName} = new ${className}(params)
    if (!${propertyName}.save()) {
      flash.type = 'error'
      flash.message = 'controllers.${packageName.toLowerCase()}.${className.toLowerCase()}.save.failed'
      render(view: "create", model: [${propertyName}: ${propertyName}])
      return
    }

    flash.type = 'success'
    flash.message = 'controllers.${packageName.toLowerCase()}.${className.toLowerCase()}.save.success'
    redirect(action: "show", id: ${propertyName}.id)
  }

  def show(Long id) {
    def ${propertyName} = ${className}.get(id)
    if (!${propertyName}) {
      flash.type = 'info'
      flash.message = 'controllers.${packageName.toLowerCase()}.${className.toLowerCase()}.show.notfound'
      return
    }

    [${propertyName}: ${propertyName}]
  }

  def edit(Long id) {
    def ${propertyName} = ${className}.get(id)
    if (!${propertyName}) {
      flash.type = 'info'
      flash.message = 'controllers.${packageName.toLowerCase()}.${className.toLowerCase()}.edit.notfound'
      redirect(action: "list")
      return
    }

    [${propertyName}: ${propertyName}]
  }

  def update(Long id, Long version) {
    def ${propertyName} = ${className}.get(id)
    if (!${propertyName}) {
      flash.type = 'info'
      flash.message = 'controllers.${packageName.toLowerCase()}.${className.toLowerCase()}.update.notfound'
      redirect(action: "list")
      return
    }

    if (version != null) {
      if (${propertyName}.version > version) {<% def lowerCaseName = grails.util.GrailsNameUtils.getPropertyName(className) %>
        ${propertyName}.errors.rejectValue("version", "controllers.${packageName.toLowerCase()}.${className.toLowerCase()}.update.optimistic.locking.failure", "${packageName.toLowerCase()}.${className.toLowerCase()} unable to lock for update as the specified object has been revised")
        render(view: "edit", model: [${propertyName}: ${propertyName}])
        return
      }
    }

    ${propertyName}.properties = params

    if (!${propertyName}.save()) {
      flash.type = 'error'
      flash.message = 'controllers.${packageName.toLowerCase()}.${className.toLowerCase()}.update.failed'
      render(view: "edit", model: [${propertyName}: ${propertyName}])
      return
    }

    flash.type = 'success'
    flash.message = 'controllers.${packageName.toLowerCase()}.${className.toLowerCase()}.update.success'
    redirect(action: "show", id: ${propertyName}.id)
  }

  def delete(Long id) {
    def ${propertyName} = ${className}.get(id)
    if (!${propertyName}) {
      flash.type = 'info'
      flash.message = 'controllers.${packageName.toLowerCase()}.${className.toLowerCase()}.update.notfound'
      redirect(action: "list")
      return
    }

    try {
      ${propertyName}.delete()
      flash.type = 'success'
      flash.message = 'controllers.${packageName.toLowerCase()}.${className.toLowerCase()}.delete.success'
      redirect(action: "list")
    }
    catch (DataIntegrityViolationException e) {
      flash.type = 'error'
      flash.message = 'controllers.${packageName.toLowerCase()}.${className.toLowerCase()}.delete.failure'
      redirect(action: "show", id: id)
    }
  }
}
