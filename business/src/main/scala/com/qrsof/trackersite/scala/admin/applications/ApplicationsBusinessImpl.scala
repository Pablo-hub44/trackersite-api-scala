package com.qrsof.trackersite.scala.admin.applications

import com.qrsof.trackersite.scala.admin.applications.dao.ApplicationsDao
import com.qrsof.trackersite.scala.admin.applications.pojos.{AddNewAppRequest, AddNewAppResponse, AppAlreadyExistsException, AppNotFound, ApplicationResponse, Applications, ApplicationsExceptions}
import com.qrsof.trackersite.scala.admin.pojos.Pageable
import com.qrsof.trackersite.scala.admin.utils.TrackerSiteUtils
import jakarta.inject.{Inject, Singleton}

@Singleton
class ApplicationsBusinessImpl @Inject() (applicationsDao: ApplicationsDao,
                                          trackerSiteUtils: TrackerSiteUtils) extends ApplicationsBusiness {


  private def buildNewApp(addNewAppRequest: AddNewAppRequest): Applications = {
    val date = trackerSiteUtils.getCurrentDate
    val active = true
    val applicationKey = trackerSiteUtils.generateKey
    val newApp = Applications(
      applicationKey,
      addNewAppRequest.userKey,
      addNewAppRequest.name,
      active,
      date,
      date
    )
    newApp
  }

   def getAppByUserAndName(userKey: String, appName: String): Option[Applications] = {
    applicationsDao.getAppByUserKeyAndName(userKey, appName)
  }

  private def appWithNameAlreadyExists(userKey: String, appName: String): Boolean = {
    getAppByUserAndName(userKey, appName) match {
      case Some(_) => true
      case None => false
    }
  }

  override def addApplication(addNewAppRequest: AddNewAppRequest): Either[ApplicationsExceptions, AddNewAppResponse] = {
    if (appWithNameAlreadyExists(addNewAppRequest.userKey, addNewAppRequest.name)) {
      Left(AppAlreadyExistsException(addNewAppRequest.name))
    } else {
      val getResponse = applicationsDao.saveNewApp(buildNewApp(addNewAppRequest))
      Right(AddNewAppResponse(getResponse.key))
    }
  }

  override def getApplicationsByUsrKey(userKey: String, pageable: Pageable): (Seq[Applications],Int) = {
    val apps = applicationsDao.getApplicationsByUsr(userKey, pageable)
    val totalApps = applicationsDao.getTotalApplicationsByUsr(userKey)
    (apps, totalApps)
  }

  override def getApplicationsByWordsFilter(userKey: String, nameApp: String): Seq[Applications] = {
    applicationsDao.getApplicationsByName(nameApp, userKey)
  }

  override def getAppByAppKey(applicationKey: String): Option[Applications] = {
    applicationsDao.getApplicationByKey(applicationKey)
  }

  private def checkAppAlreadyExistByAppKey(applicationKey: String): Boolean = {
    getAppByAppKey(applicationKey) match {
      case Some(_) => true
      case None => false
    }
  }

  override def logicDeleteAppByKey(applicationKey: String): Either[ApplicationsExceptions, ApplicationResponse] = {
    if (checkAppAlreadyExistByAppKey(applicationKey)) {
      val deleteApp: String = applicationsDao.logicDeleteAppByAppKey(applicationKey)
      Right(ApplicationResponse(deleteApp))
    } else {
      Left(AppNotFound(applicationKey))
    }
  }
}
