package com.qrsof.trackersite.scala.admin.applications.dao

import com.qrsof.trackersite.scala.admin.applications.pojos.Applications
import com.qrsof.trackersite.scala.admin.pojos.Pageable

trait ApplicationsDao {

  def saveNewApp(applications: Applications): Applications

  def getAppByUserKeyAndName(userKey: String, appName: String): Option[Applications]

  def getApplicationsByUsr(userKey: String, pageable: Pageable): Seq[Applications]

  def getTotalApplicationsByUsr(userKey: String): Int

  def getApplicationByKey(applicationKey: String): Option[Applications]

  def getApplicationsByName(nameApp: String, userKey: String): Seq[Applications]

  def logicDeleteAppByAppKey(applicationKey: String): String

}
