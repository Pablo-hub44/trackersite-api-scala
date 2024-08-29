package com.qrsof.trackersite.scala.daos

import com.qrsof.core.database.qrslick.QueryExecutor
import com.qrsof.trackersite.scala.admin.user.dao.UserDao
import com.qrsof.trackersite.scala.admin.user.pojos.User
import com.qrsof.trackersite.scala.tables.UsersTable
import jakarta.inject.Inject
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext

class UserDaoImpl @Inject() (queryExecutor: QueryExecutor)(implicit ex: ExecutionContext) extends UserDao {

  val logger: Logger = LoggerFactory.getLogger(classOf[UserDaoImpl])

  override def saveUser(user: User): User = queryExecutor.syncExecuteQuery {
    logger.info("UserDaoImpl: saveUser")
    UsersTable.save(user)
  }

  override def getUserInfoByUserKey(userKey: String): User = queryExecutor.syncExecuteQuery {
    logger.info("Try to foun user data")
    UsersTable.findById(userKey)
  }
}
