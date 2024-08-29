package com.qrsof.trackersite.scala.admin.utils.files

import com.qrsof.trackersite.scala.admin.utils.files.digitalOcean.DigitalOceanResources
import com.qrsof.trackersite.scala.admin.utils.files.pojos.{FileResource, ViewFileExceptions}
import jakarta.inject.Inject

import scala.concurrent.ExecutionContext

class FilesDigitalOceanActionsImpl @Inject()(
                                              digitalOceanResources: DigitalOceanResources
                                            )(implicit ec: ExecutionContext) extends FilesActions {

  override def saveFile(events: Seq[String]): String = ???

  override def getFile(fileKey: String, recordingKey: String, contentType: String): Either[ViewFileExceptions, FileResource] = ???

  override def updateFile(fileKey: String, newEvents: Seq[String]): Either[ViewFileExceptions, String] = ???
}
