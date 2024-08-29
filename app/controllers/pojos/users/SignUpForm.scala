package controllers.pojos.users

import play.api.libs.json.{Json, OFormat}

case class SignUpForm(name: String,
                      lastName: String,
                      username: String,
                      email: String,
                      password: String)

object SignUpForm {
  implicit val format: OFormat[SignUpForm] = Json.format[SignUpForm]
}
