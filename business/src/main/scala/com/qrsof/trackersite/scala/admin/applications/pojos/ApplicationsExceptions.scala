package com.qrsof.trackersite.scala.admin.applications.pojos

import com.qrsof.core.app.errors.AppError
import com.qrsof.trackersite.scala.admin.applications.pojos.AppErrorCodes.{AppAlreadyExistsError, AppNotFoundError}

sealed abstract class ApplicationsExceptions(val errorCode: AppError)

case class AppAlreadyExistsException(AppName: String) extends ApplicationsExceptions(AppAlreadyExistsError(AppName))

case class AppNotFound(applicationKey: String) extends ApplicationsExceptions(AppNotFoundError(applicationKey))


object AppErrorCodes {

  case class AppAlreadyExistsError(AppName: String) extends AppError {
    override val code: String = "APP01"
    override val error: Option[String] = Some("Application already exists")
    override val detail: Option[String] = Some(AppName)
  }

  case class AppNotFoundError(applicationKey: String) extends AppError {
    override val code: String = "APP02"
    override val error: Option[String] = Some("Application not found")
    override val detail: Option[String] = Some(applicationKey)
  }

}
