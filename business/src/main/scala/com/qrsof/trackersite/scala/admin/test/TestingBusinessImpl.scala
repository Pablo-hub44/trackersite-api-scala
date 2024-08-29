package com.qrsof.trackersite.scala.admin.test

import com.qrsof.trackersite.scala.admin.test.dao.TestsDao
import com.qrsof.trackersite.scala.admin.test.pojos.Tests
import jakarta.inject.{Inject, Singleton}

@Singleton
class TestingBusinessImpl @Inject() (testsDao: TestsDao) extends TestingBusiness {

  override def getTests: Seq[Tests] = {
    testsDao.getTests
  }
}
