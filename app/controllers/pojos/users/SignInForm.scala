package controllers.pojos.users

import play.api.libs.json.{Json, OFormat}

case class SignInForm(username: String, password: String)

object SignInForm {
  implicit val format: OFormat[SignInForm] = Json.format[SignInForm]
}
