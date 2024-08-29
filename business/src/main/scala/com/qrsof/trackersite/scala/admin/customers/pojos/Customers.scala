package com.qrsof.trackersite.scala.admin.customers.pojos

import java.util.Date

case class Customers(
                    key: String,
                    active: Boolean,
                    createdAt: Date,
                    updatedAt: Date,
                    userKey: String
                    )
