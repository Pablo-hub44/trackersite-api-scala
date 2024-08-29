package com.qrsof.trackersite.scala.daos

import com.qrsof.core.database.qrslick.QrPostgresProfile.api._
import com.qrsof.core.database.qrslick.QueryExecutor
import com.qrsof.trackersite.scala.admin.customers.dao.CustomersDao
import com.qrsof.trackersite.scala.admin.customers.pojos.Customers
import com.qrsof.trackersite.scala.tables.CustomersTable
import jakarta.inject.{Inject, Singleton}

import scala.concurrent.ExecutionContext


@Singleton
class CustomersDaoImpl @Inject()(queryExecutor: QueryExecutor)(implicit ex: ExecutionContext) extends CustomersDao{

  override def getCustomersByKey(clientKey: String): Option[Customers] = queryExecutor.syncExecuteQuery {
    CustomersTable.findOptionByProperty(customer => customer.key === clientKey && customer.active === true)
  }
}
