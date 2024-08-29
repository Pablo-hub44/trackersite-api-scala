package com.qrsof.trackersite.scala.admin.customers.dao

import com.qrsof.trackersite.scala.admin.customers.pojos.Customers

trait CustomersDao {

  def getCustomersByKey(clientKey: String): Option[Customers]

}
