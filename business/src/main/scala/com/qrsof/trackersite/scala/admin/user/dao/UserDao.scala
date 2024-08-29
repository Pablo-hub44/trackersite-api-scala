package com.qrsof.trackersite.scala.admin.user.dao

import com.qrsof.trackersite.scala.admin.user.pojos.User


trait UserDao {

  def saveUser(user: User): User
  def getUserInfoByUserKey(userKey: String): User


}
