package com.qrsof.trackersite.scala.admin.user

import com.qrsof.core.app.errors.AppError
import com.qrsof.core.app.exceptions.ApplicationException
import com.qrsof.trackersite.scala.admin.user.OauthErrorCodes._

sealed abstract class OauthException(errorCode: AppError) extends ApplicationException(errorCode)

object OauthExceptionObj {
  case class OauthUserNotFoundException(username: String) extends OauthException(UserNotFoundErrorCode(username))

  case class UserCredentialsException(username: String) extends OauthException(UserCredentialsErrorCode(username))

  case class UserAlreadyExistsException(username: String) extends OauthException(UserAlreadyExistsErrorCode(username))

  case class MalformedGoogleTokenException(userResource: String) extends OauthException(MalformedGoogleTokenErrorCode(userResource))


  case class UnknownException(username: String) extends OauthException(UnknowErrorCode(username))
}
object OauthErrorCodes {
  case class UserNotFoundErrorCode(username: String) extends AppError {
    override val code: String = "OAC01"
    override val error: Option[String] = Some("User not found")
    override val detail: Option[String] = Some(username)
  }
  case class UserCredentialsErrorCode(username: String) extends AppError {
    override val code: String = "OAC02"
    override val error: Option[String] = Some("Credential Invalid")
    override val detail: Option[String] = Some(username)
  }
  case class UserAlreadyExistsErrorCode(username: String) extends AppError {
    override val code: String = "OAC03"
    override val error: Option[String] = Some("User already exists")
    override val detail: Option[String] = Some(username)
  }
  case class UnknowErrorCode(username: String) extends AppError {
    override val code: String = "OAC05"
    override val error: Option[String] = Some("Unexpected error")
    override val detail: Option[String] = Some(username)
  }

  case class MalformedGoogleTokenErrorCode(userResource: String) extends AppError {
    override val code: String = "OAC07"
    override val error: Option[String] = Some("Malformed google sso token")
    override val detail: Option[String] = Some(userResource)
  }
}
