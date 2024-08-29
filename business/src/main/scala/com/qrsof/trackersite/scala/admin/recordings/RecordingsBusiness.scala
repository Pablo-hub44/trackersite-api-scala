package com.qrsof.trackersite.scala.admin.recordings

import com.qrsof.trackersite.scala.admin.pojos.Pageable
import com.qrsof.trackersite.scala.admin.recordings.pojos.{AddNewRecordingRequest, AddNewRecordingResponse, Recordings, RecordingsExceptions}

import java.sql.Timestamp


trait RecordingsBusiness {

  def getRecordingsByAppKey(userKey: String, applicationKey: String, pageable: Pageable, startDate: Option[Timestamp], endDate: Option[Timestamp]): (Seq[Recordings], Int)

  def getRecordingByKey(userKey: String, recordingKey: String): Option[Recordings]

  def addRecordingByApp(addNewRecordingRequest:AddNewRecordingRequest): Either[RecordingsExceptions, AddNewRecordingResponse]

}
