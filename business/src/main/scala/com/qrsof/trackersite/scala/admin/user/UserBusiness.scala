package com.qrsof.trackersite.scala.admin.user

import com.qrsof.jwt.models.JwtToken
import com.qrsof.trackersite.scala.admin.user.pojos.{SignInRequest, SignUpRequest, User}

trait UserBusiness {

  def signUpUser(signUpRequest: SignUpRequest): Either[OauthException, JwtToken]
  def signInUser(signInRequest: SignInRequest): Either[OauthException, JwtToken]
  def getDataUserByUserKey(userKey: String): User

}
