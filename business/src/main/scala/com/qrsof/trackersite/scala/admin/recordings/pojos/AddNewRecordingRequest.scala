package com.qrsof.trackersite.scala.admin.recordings.pojos

case class AddNewRecordingRequest(
                                 applicationKey: String,
                                 events: Array[String],
                                 clientKey: String,
                                 recordingKey: Option[String]
                                 )
