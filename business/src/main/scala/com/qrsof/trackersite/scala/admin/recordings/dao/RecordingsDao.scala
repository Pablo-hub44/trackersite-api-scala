package com.qrsof.trackersite.scala.admin.recordings.dao

import com.qrsof.trackersite.scala.admin.pojos.Pageable
import com.qrsof.trackersite.scala.admin.recordings.pojos.Recordings

import java.sql.Timestamp


trait RecordingsDao {

  def getRecordingsByUserAndAppKey(userKey: String, applicationKey: String, pageable: Pageable, startDate: Option[Timestamp], endDate: Option[Timestamp]): Seq[Recordings]
  def getRecordingByKey(userKey: String, recordingKey: String): Option[Recordings]
  def saveNewRecording(recordings: Recordings): Recordings
  def getTotalRecordingsByUserAndAppKey(userKey: String, applicationKey: String): Int
  def getReferenceFileByRecordingKey(recordingKey: String): Option[Recordings]

}
