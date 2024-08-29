package controllers.api.applications

import com.qrsof.core.app.api.play.BasePlayController
import com.qrsof.core.app.api.play.security.AuthenticatedAction
import com.qrsof.trackersite.scala.admin.applications.ApplicationsBusiness
import com.qrsof.trackersite.scala.admin.applications.pojos.{AddNewAppRequest, AddNewAppResponse, AppAlreadyExistsException, Applications, ApplicationsExceptions}
import com.qrsof.trackersite.scala.admin.pojos.Pageable
import controllers.pojos.applications.{AddApplicationForm, ApplicationsResponse, ApplicationsResponseWithTotal}
import io.scalaland.chimney.dsl._
import jakarta.inject.Inject
import play.api.mvc._

class ApplicationControllerImpl @Inject()(cc: ControllerComponents,
                                          applicationsBusiness: ApplicationsBusiness,
                                          authenticatedAction: AuthenticatedAction) extends AbstractController(cc) with ApplicationController with BasePlayController {


  def getApplicationsByUsrKey(page: Int, pageSize: Int): Action[AnyContent] = authenticatedAction { implicit request =>
    val userKey = request.principal
        val (applicationsList, totalApplications) = applicationsBusiness.getApplicationsByUsrKey(userKey, Pageable(page, pageSize))
        val orderApps = applicationsList.map(_.transformInto[ApplicationsResponse]).sortBy(_.createdAt).reverse
        val response = ApplicationsResponseWithTotal(totalApplications, orderApps)
        Ok(response.toJsValue)

  }

  def addNewApplication(): Action[AnyContent] = authenticatedAction { implicit request =>
    val addApplicationForm = bindFromRequest[AddApplicationForm]().get
    val userKey = request.principal

    val addNewAppRequest = addApplicationForm.into[AddNewAppRequest]
      .withFieldConst(_.userKey, userKey)
      .transform

    applicationsBusiness.addApplication(addNewAppRequest) match {
      case Left(error) => error match {
        case AppAlreadyExistsException(name) => Conflict(name.toJsValue)
      }
      case Right(respAddApp: AddNewAppResponse) =>
        Ok(respAddApp.toJsValue)
    }
  }


  def getApplicationByKey (applicationKey: String): Action[AnyContent] = authenticatedAction { implicit request =>
    val userKey = request.principal
        val application: Option[Applications] = applicationsBusiness.getAppByAppKey(applicationKey)
        val applicationFind = application.map(_.transformInto[ApplicationsResponse])
        Ok(applicationFind.toJsValue)
  }

  def logicDeleteApplication (applicationKey: String): Action[AnyContent] = authenticatedAction { implicit request =>
        applicationsBusiness.logicDeleteAppByKey(applicationKey) match {
          case Left(logicDeleteException: ApplicationsExceptions) =>
            NotFound(logicDeleteException.toJsValue)
          case Right(response) =>
            Ok(response.toJsValue)
        }
  }

  def getApplicationsByWordFilter(nameApp: String): Action[AnyContent] = authenticatedAction { implicit request =>
    val userKey = request.principal
        val filteredApps = applicationsBusiness.getApplicationsByWordsFilter(userKey, nameApp)
        Ok(filteredApps.toJsValue)
  }

}
