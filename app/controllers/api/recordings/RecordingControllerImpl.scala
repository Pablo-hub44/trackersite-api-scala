package controllers.api.recordings

import com.qrsof.core.app.api.play.BasePlayController
import com.qrsof.core.app.api.play.security.AuthenticatedAction
import com.qrsof.trackersite.scala.admin.pojos.Pageable
import com.qrsof.trackersite.scala.admin.recordings.RecordingsBusiness
import com.qrsof.trackersite.scala.admin.recordings.pojos.{AddNewRecordingRequest, AppNotFound, RecordingNotFoundError, UserNotAuth}
import com.qrsof.trackersite.scala.admin.utils.files.FilesActions
import com.qrsof.trackersite.scala.admin.utils.files.pojos.ViewFileExceptions
import controllers.pojos.ApiErrorResponse
import controllers.pojos.recordings.{AddRecordingForm, DetailRecording, RecordingsResponse, RecordingsResponseWithTotal}
import io.scalaland.chimney.dsl._
import jakarta.inject.Inject
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Result}

import java.sql.Timestamp
import java.time.{LocalDate, ZoneId}
import java.time.format.DateTimeFormatter
import java.util.Date
import scala.io.Source

class RecordingControllerImpl @Inject()(cc: ControllerComponents, recordingsBusiness: RecordingsBusiness,
                                        filesActions: FilesActions,
                                        authenticatedAction: AuthenticatedAction)
                                     extends AbstractController(cc) with RecordingController with BasePlayController {

  def getRecordingsByAppKey(appKey: String, page: Int, pageSize: Int,
                            startDate: Option[String], endDate: Option[String]): Action[AnyContent] = authenticatedAction { implicit request =>
    val startTimestamp = startDate.flatMap(parseDate)
    val endTimestamp = endDate.flatMap(parseDate)
    val userKey = request.principal
        val (recordingsList, totalRecordings) = recordingsBusiness.getRecordingsByAppKey(userKey, appKey, Pageable(page, pageSize), startTimestamp, endTimestamp)
        val recordingsComplete = recordingsList.map(_.transformInto[RecordingsResponse])
        val orderRecordings = recordingsComplete.sortBy(_.createdAt).reverse
        val response = RecordingsResponseWithTotal(totalRecordings, orderRecordings)
        Ok(response.toJsValue)
  }

  private def parseDate(dateString: String): Option[Timestamp] = {
    try {
      val date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
      val timestamp = Timestamp.valueOf(date.atStartOfDay(ZoneId.systemDefault()).toLocalDateTime)
      Some(timestamp)
    } catch {
      case _: Throwable => None
    }
  }

  def getRecordingByKey(recordingKey: String): Action[AnyContent] = authenticatedAction { implicit request =>
    val userKey = request.principal
        recordingsBusiness.getRecordingByKey(userKey, recordingKey) match {
          case Some(recordingValue) =>
            getFileRoute(s"${recordingValue.eventsReference}.json", recordingKey, recordingValue.createdAt, recordingValue.updatedAt)
          case None =>
            val error = RecordingNotFoundError(recordingKey)
            NotFound(error.toJsValue)
        }
  }

  private def getFileRoute(reference: String, recordingKey: String, createdAt: Date, updatedAt: Date): Result = {
    filesActions.getFile(reference, recordingKey, "") match {
      case Left(viewFileExceptions: ViewFileExceptions) =>
        NotFound(viewFileExceptions.toJsValue)
      case Right(fileResource) =>
        val referenceWithoutExtension = if (reference.endsWith(".json")) reference.dropRight(5) else reference
        val recordingsResponse = RecordingsResponse(recordingKey, referenceWithoutExtension, createdAt, updatedAt)
        val content: String = Source.fromInputStream(fileResource.content).mkString
        val detailResponse = DetailRecording(recordingsResponse, content)
        Ok(detailResponse.toJsValue)
    }
  }

  def addNewRecordingIntoApp(): Action[AnyContent] = Action { implicit request =>
    val appKey = request.headers.get("Application-Key")
    val clientKey = request.headers.get("Client-Key")

    (appKey, clientKey) match {
      case (Some(appKey), Some(clientKey)) =>
        val addRecordingForm = bindFromRequest[AddRecordingForm]().get
        val addNewRecordingRequest = AddNewRecordingRequest(
          applicationKey = appKey,
          events = addRecordingForm.events,
          clientKey = clientKey,
          recordingKey = addRecordingForm.recordingKey
        )

        recordingsBusiness.addRecordingByApp(addNewRecordingRequest) match {
          case Left(UserNotAuth(userKey)) => Unauthorized(userKey.toJsValue)
          case Left(AppNotFound(applicationKey)) => NotFound(applicationKey.toJsValue)
          case Left(RecordingNotFoundError(recordingKey)) => NotFound(recordingKey.toJsValue)
          case Right(responseAddRecording) =>
            Ok(responseAddRecording.toJsValue)
        }
      case _ => BadRequest(Json.toJson(ApiErrorResponse(Seq("Missing appKey or clientKey in headers"))))
    }
  }

}
