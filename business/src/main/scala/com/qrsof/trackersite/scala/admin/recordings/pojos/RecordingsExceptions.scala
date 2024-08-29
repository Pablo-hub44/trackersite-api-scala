package com.qrsof.trackersite.scala.admin.recordings.pojos

import com.qrsof.core.app.errors.AppError
import com.qrsof.trackersite.scala.admin.recordings.pojos.AppErrorCodes.{AppNotFoundError, RecordingNotFound, UserNotAuthorized}

sealed abstract class RecordingsExceptions(val errorCode: AppError)


case class AppNotFound(applicationKey: String) extends RecordingsExceptions(AppNotFoundError(applicationKey))

case class UserNotAuth(userKey: String) extends RecordingsExceptions(UserNotAuthorized(userKey))

case class RecordingNotFoundError(recordingKey: String) extends RecordingsExceptions(RecordingNotFound(recordingKey))

object AppErrorCodes {

  case class AppNotFoundError(applicationKey: String) extends AppError {
    override val code: String = "RA01"
    override val error: Option[String] = Some("Application not found")
    override val detail: Option[String] = Some(applicationKey)
  }

  case class UserNotAuthorized(userKey: String) extends AppError {
    override val code: String = "RA02"
    override val error: Option[String] = Some("User without authorization")
    override val detail: Option[String] = Some(userKey)
  }

  case class RecordingNotFound(recordingKey: String) extends AppError {
    override val code: String = "RA03"
    override val error: Option[String] = Some("Recording not found")
    override val detail: Option[String] = Some(recordingKey)
  }
}
