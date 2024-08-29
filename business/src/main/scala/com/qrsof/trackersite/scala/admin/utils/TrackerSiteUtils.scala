package com.qrsof.trackersite.scala.admin.utils

import java.sql.Timestamp

trait TrackerSiteUtils {

  def generateKey: String

  def getCurrentDate: Timestamp

}
