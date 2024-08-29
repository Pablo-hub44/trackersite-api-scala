package controllers.api.status

import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import javax.inject.Inject

class StatusControllerImpl @Inject()(cc: ControllerComponents) extends AbstractController(cc) with StatusController {

  def status: Action[AnyContent] = Action {
    Ok("Alive")
  }
}
