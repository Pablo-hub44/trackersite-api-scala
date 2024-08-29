package controllers.api.swagger

import com.qrsof.trackersite.scala.AppConfigurations
import jakarta.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

@Singleton
class ApiSwaggerControllerImpl @Inject() (cc: ControllerComponents,
                                          swaggerHttpServiceImpl: SwaggerHttpServiceImpl,
                                          appConfigurations: AppConfigurations
                                         ) extends AbstractController(cc)   with ApiSwaggerController {

  def swaggerSpec(): Action[AnyContent] = Action { implicit request =>
    Ok(swaggerHttpServiceImpl.getJsonSwagger()).as(JSON)
  }

  def swaggerUi: Action[AnyContent] = Action {
    Redirect("https://petstore.swagger.io/?url=" + appConfigurations.apiHost + "/api-docs/swagger.json")
  }

}
