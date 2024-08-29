package com.qrsof.trackersite.scala.admin.user.pojos

import play.api.libs.json.{Json, OFormat}

import java.util.Date


case class User(key: String,
                email: String,
                name: String,
                lastName: String,
                userName: String,
                createdAt: Date,
                updatedAt: Date)

object User {
  implicit val userFormat: OFormat[User] = Json.format[User]
}
