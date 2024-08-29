package com.qrsof.trackersite.scala.admin.recordings.pojos

import java.sql.Timestamp


case class Recordings(
                       key: String,
                       eventsReference: String,
                       createdAt: Timestamp,
                       updatedAt: Timestamp,
                       applicationKey: String
                     )
