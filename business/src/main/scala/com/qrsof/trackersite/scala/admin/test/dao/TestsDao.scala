package com.qrsof.trackersite.scala.admin.test.dao

import com.qrsof.trackersite.scala.admin.test.pojos.Tests

trait TestsDao {

  def getTests: Seq[Tests]

}
