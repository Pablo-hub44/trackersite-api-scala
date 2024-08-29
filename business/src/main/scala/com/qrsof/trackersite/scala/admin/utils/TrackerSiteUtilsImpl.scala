package com.qrsof.trackersite.scala.admin.utils

import com.fasterxml.uuid.Generators
import jakarta.inject.{Inject, Singleton}

import java.sql.Timestamp


@Singleton
class TrackerSiteUtilsImpl @Inject() () extends TrackerSiteUtils {

  override def generateKey: String = {
    Generators.timeBasedEpochGenerator().generate().toString
  }

  override def getCurrentDate: Timestamp = {
    new Timestamp(System.currentTimeMillis())
  }
}
