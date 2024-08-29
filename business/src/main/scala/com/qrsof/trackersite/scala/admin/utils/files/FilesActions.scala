package com.qrsof.trackersite.scala.admin.utils.files

import com.qrsof.trackersite.scala.admin.utils.files.pojos.{FileResource, ViewFileExceptions}



trait FilesActions {

  def getFile(fileKey: String, recordingKey: String, contentType: String): Either[ViewFileExceptions, FileResource]

  def saveFile(events: Seq[String]): String
  def updateFile(fileKey: String, newEvents: Seq[String]): Either[ViewFileExceptions, String]

}
