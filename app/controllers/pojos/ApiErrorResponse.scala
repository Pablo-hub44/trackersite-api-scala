package controllers.pojos

import play.api.libs.json.{Json, OFormat}

case class ApiErrorResponse(errors: Seq[String])
object ApiErrorResponse {
  implicit val apiErrorResponseFormat: OFormat[ApiErrorResponse] = Json.format[ApiErrorResponse]
}
