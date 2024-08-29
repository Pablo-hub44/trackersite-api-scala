package com.qrsof.trackersite.scala.admin.applications.pojos

import java.util.Date

case class Applications(
                       key: String,
                       userKey: String,
                       name: String,
                       active: Boolean,
                       createdAt: Date,
                       updatedAt: Date
                       )
