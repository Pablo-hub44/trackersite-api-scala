package controllers.api.swagger

import play.api.mvc.{Action, AnyContent}

trait ApiSwaggerController {

  def swaggerSpec(): Action[AnyContent]

  def swaggerUi: Action[AnyContent]

}
