package com.qrsof.trackersite.scala.admin.utils.files.pojos

import com.qrsof.core.app.errors.AppError
import com.qrsof.core.app.exceptions.ApplicationException
import com.qrsof.trackersite.scala.admin.utils.files.pojos.ViewFileErrorCodes.{FileInLocalNotFoundError, FileInSs3NotFoundError}

sealed abstract class ViewFileExceptions (override val appError: AppError) extends ApplicationException(appError)

case class FileInSs3NotFoundException(fileKey: String) extends ViewFileExceptions(FileInSs3NotFoundError(fileKey))

case class FileInLocalNotFoundException(fileKey: String) extends ViewFileExceptions(FileInLocalNotFoundError(fileKey))

object ViewFileErrorCodes {
  case class FileInSs3NotFoundError(fileKey: String) extends AppError{
    override val code: String = "VFE01"
    override val error: Option[String] = Some("File in S3 not found")
    override val detail: Option[String] = Some(fileKey)
  }

  case class FileInLocalNotFoundError(fileKey: String) extends AppError{
    override val code: String = "VFE02"
    override val error: Option[String] = Some("File in Local not found")
    override val detail: Option[String] = Some(fileKey)
  }
}
