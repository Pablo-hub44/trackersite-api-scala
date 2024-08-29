package com.qrsof.trackersite.scala.admin.utils.files.digitalOcean

import com.qrsof.trackersite.scala.admin.utils.files.pojos.{FileKey, FileResource}
import org.apache.pekko.http.scaladsl.common.StrictForm.FileData

import scala.concurrent.Future

trait DigitalOceanResources {

  def save(folder: String, fileData: FileData): Future[Option[FileKey]]

  def get(resourceKey: String, applicationKey: String): Future[Option[FileResource]]

}
