package com.qrsof.trackersite.scala.admin.applications

import com.qrsof.trackersite.scala.admin.applications.pojos.{AddNewAppRequest, AddNewAppResponse, ApplicationResponse, Applications, ApplicationsExceptions}
import com.qrsof.trackersite.scala.admin.pojos.Pageable

trait ApplicationsBusiness {

  def addApplication(addNewAppRequest: AddNewAppRequest): Either[ApplicationsExceptions, AddNewAppResponse]
  def getApplicationsByUsrKey(userKey: String, pageable: Pageable): (Seq[Applications], Int)
  def getApplicationsByWordsFilter(userKey: String, nameApp: String): Seq[Applications]
  def getAppByAppKey(applicationKey: String): Option[Applications]
  def logicDeleteAppByKey(applicationKey: String): Either[ApplicationsExceptions, ApplicationResponse]
}
