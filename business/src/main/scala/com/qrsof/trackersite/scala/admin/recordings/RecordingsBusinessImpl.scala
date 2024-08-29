package com.qrsof.trackersite.scala.admin.recordings

import com.qrsof.trackersite.scala.admin.applications.dao.ApplicationsDao
import com.qrsof.trackersite.scala.admin.customers.dao.CustomersDao
import com.qrsof.trackersite.scala.admin.pojos.Pageable
import com.qrsof.trackersite.scala.admin.recordings.dao.RecordingsDao
import com.qrsof.trackersite.scala.admin.recordings.pojos._
import com.qrsof.trackersite.scala.admin.utils.TrackerSiteUtils
import com.qrsof.trackersite.scala.admin.utils.files.FilesActions
import jakarta.inject.{Inject, Singleton}
import org.slf4j.{Logger, LoggerFactory}

import java.sql.Timestamp

@Singleton
class RecordingsBusinessImpl @Inject() (
                                         recordingsDao: RecordingsDao,
                                         applicationsDao: ApplicationsDao,
                                         customersDao: CustomersDao,
                                         filesActions: FilesActions,
                                         trackerSiteUtils: TrackerSiteUtils
                                       ) extends RecordingsBusiness {

  private val logger: Logger = LoggerFactory.getLogger(classOf[RecordingsBusinessImpl])

  override def getRecordingsByAppKey(userKey: String, applicationKey: String, pageable: Pageable, startDate: Option[Timestamp], endDate: Option[Timestamp]): (Seq[Recordings], Int) = {
    val recordings = recordingsDao.getRecordingsByUserAndAppKey(userKey, applicationKey, pageable, startDate, endDate)
    val totalRecordings = recordingsDao.getTotalRecordingsByUserAndAppKey(userKey, applicationKey)
    (recordings, totalRecordings)
  }

  override def getRecordingByKey(userKey: String, recordingKey: String): Option[Recordings] = {
    recordingsDao.getRecordingByKey(userKey, recordingKey)
  }

  private def buildNewRecording(addNewRecordingRequest: AddNewRecordingRequest): Recordings = {
    val date = trackerSiteUtils.getCurrentDate
    val recordingKey = trackerSiteUtils.generateKey
    val eventsFileName = filesActions.saveFile(addNewRecordingRequest.events)

    val newRecording = Recordings(
      recordingKey,
      eventsFileName,
      date,
      date,
      addNewRecordingRequest.applicationKey
    )
    newRecording
  }
  private def validationCustomerActive(clientKey: String): Boolean = {
    customersDao.getCustomersByKey(clientKey).isDefined
  }

  private def validationAppExist(applicationKey: String): Boolean = {
    applicationsDao.getApplicationByKey(applicationKey).isDefined
  }

  private def getInfoRecording(recordingKey: String): Option[Recordings] = {
    recordingsDao.getReferenceFileByRecordingKey(recordingKey)
  }

  override def addRecordingByApp(request: AddNewRecordingRequest): Either[RecordingsExceptions, AddNewRecordingResponse] = {
    (validationCustomerActive(request.clientKey), validationAppExist(request.applicationKey)) match {
      case (false, _) => Left(UserNotAuth(request.clientKey))
      case (_, false) => Left(AppNotFound(request.applicationKey))
      case _ =>
        if (request.recordingKey.isDefined) {
          val recordOpt = getInfoRecording(request.recordingKey.get)
          recordOpt match {
            case Some(record) =>
              updateRecording(record, request.events) match {
                case Right(_) =>
                  Right(AddNewRecordingResponse(recordingKey = record.key))
                case Left(exception) =>
                  Left(exception)
              }
            case None =>
              Left(RecordingNotFoundError(request.recordingKey.get))
          }
        } else {
          val newRecording = buildNewRecording(request)
          val saveRecording = recordingsDao.saveNewRecording(newRecording)
          logger.info("Saving recording {}", saveRecording)
          Right(AddNewRecordingResponse(saveRecording.key))
        }
    }
  }

  private def updateRecording(record: Recordings, newEvents: Seq[String]): Either[RecordingsExceptions, AddNewRecordingResponse] = {
    try {
      filesActions.updateFile(s"${record.eventsReference}.json", newEvents) match {
        case Right(_) =>
          logger.info(s"Recording ${record.key} updated successfully")
          Right(AddNewRecordingResponse(record.key))
        case Left(exception) =>
          logger.error(s"Error updating recording ${record.key}", exception)
          Left(RecordingNotFoundError(record.key))
      }
    } catch {
      case e: Throwable =>
        logger.error(s"Failed to update recording ${record.key}", e)
        Left(RecordingNotFoundError(record.key))
    }
  }
}
