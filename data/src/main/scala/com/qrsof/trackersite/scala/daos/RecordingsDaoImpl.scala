package com.qrsof.trackersite.scala.daos

import com.qrsof.core.database.qrslick.QrPostgresProfile.api._
import com.qrsof.core.database.qrslick.QueryExecutor
import com.qrsof.trackersite.scala.admin.pojos.{Pageable => PageableB}
import com.qrsof.trackersite.scala.admin.recordings.dao.RecordingsDao
import com.qrsof.trackersite.scala.admin.recordings.pojos.Recordings
import com.qrsof.trackersite.scala.tables.{ApplicationsTable, RecordingsTable}
import jakarta.inject.{Inject, Singleton}

import java.sql.Timestamp
import java.util.Calendar
import scala.collection.mutable
import scala.concurrent.ExecutionContext

@Singleton
class RecordingsDaoImpl @Inject() (queryExecutor: QueryExecutor)(implicit ex: ExecutionContext) extends RecordingsDao {

  def startOfDay(timestamp: Timestamp): Timestamp = {
    val cal = Calendar.getInstance()
    cal.setTime(timestamp)
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    new Timestamp(cal.getTimeInMillis)
  }

  def endOfDay(timestamp: Timestamp): Timestamp = {
    val cal = Calendar.getInstance()
    cal.setTime(timestamp)
    cal.set(Calendar.HOUR_OF_DAY, 23)
    cal.set(Calendar.MINUTE, 59)
    cal.set(Calendar.SECOND, 59)
    cal.set(Calendar.MILLISECOND, 999)
    new Timestamp(cal.getTimeInMillis)
  }

  def buildQueryCriteria(userKey: String, applicationKey: String, startDate: Option[Timestamp], endDate: Option[Timestamp]):
  Query[RecordingsTable.EntityTable, RecordingsTable.EntityTable#TableElementType, Seq] = {

    val startDay = startDate.map(startOfDay)
    val endDay = endDate.map(endOfDay)

    val baseQuery = for {
      (recordings, application) <- RecordingsTable.tableQuery join ApplicationsTable.tableQuery on (_.applicationKey === _.key)
      if recordings.applicationKey === applicationKey && application.userKey === userKey
    } yield recordings

    val queryWithDateRange = (startDay, endDay) match {
      case (Some(start), Some(end)) =>
        baseQuery.filter(recording => recording.createdAt >= start && recording.createdAt <= end)
      case _ => baseQuery
    }

    queryWithDateRange
  }

  def getRecordingsByUserAndAppKey(userKey: String, applicationKey: String, pageableB: PageableB, startDate: Option[Timestamp], endDate: Option[Timestamp]): Seq[Recordings] = {
    val pageNumber = pageableB.pageNumber
    val pageSize = pageableB.pageSize
    val offset = pageNumber * pageSize

    val query = buildQueryCriteria(userKey, applicationKey, startDate, endDate)

    queryExecutor.syncExecuteQuery {
      query.drop(offset).take(pageSize).result
    }
  }

  override def getRecordingByKey(userKey: String, recordingKey: String): Option[Recordings] = queryExecutor.syncExecuteQuery {
    RecordingsTable.findOptionByProperty(_.key === recordingKey)
  }

  override def saveNewRecording(recordings: Recordings): Recordings = {
    val transactions = mutable.ListBuffer.empty[DBIO[Any]]
    transactions += RecordingsTable.save(recordings)
    val transRecordingsSeq: Seq[DBIOAction[_, NoStream, _]] = transactions.toSeq
    queryExecutor.syncExecuteQuery(DBIO.seq(transRecordingsSeq: _*))
    recordings
  }

  override def getTotalRecordingsByUserAndAppKey(userKey: String, applicationKey: String): Int = {
    val baseQuery = for {
      (recordings, application) <- RecordingsTable.tableQuery join ApplicationsTable.tableQuery on (_.applicationKey === _.key)
      if recordings.applicationKey === applicationKey && application.userKey === userKey
    } yield recordings

    queryExecutor.syncExecuteQuery {
      baseQuery.length.result
    }
  }

  override def getReferenceFileByRecordingKey(recordingKey: String): Option[Recordings] = queryExecutor.syncExecuteQuery {
    RecordingsTable.findOptionByProperty(_.key === recordingKey)
  }
}
