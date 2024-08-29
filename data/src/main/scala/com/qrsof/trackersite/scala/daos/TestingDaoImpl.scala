package com.qrsof.trackersite.scala.daos

import com.qrsof.core.database.qrslick.QrPostgresProfile.api._
import com.qrsof.core.database.qrslick.QueryExecutor
import com.qrsof.trackersite.scala.admin.test.dao.TestsDao
import com.qrsof.trackersite.scala.admin.test.pojos.Tests
import com.qrsof.trackersite.scala.tables.TestTable
import jakarta.inject.{Inject, Singleton}
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext

@Singleton
class TestingDaoImpl @Inject() (queryExecutor: QueryExecutor)(implicit ex: ExecutionContext) extends TestsDao {

  val logger: Logger = LoggerFactory.getLogger(classOf[TestingDaoImpl])


  override def getTests: Seq[Tests] = queryExecutor.syncExecuteQuery {
    TestTable.list
  }
}
