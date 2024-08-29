package com.qrsof.trackersite.scala.admin.user

import com.qrsof.apptack.client.ApptackClient
import com.qrsof.jwt.models.{DecodedToken, JwtToken}
import com.qrsof.jwt.validation.JwtValidationService
import com.qrsof.trackersite.scala.admin.user.OauthExceptionObj._
import com.qrsof.trackersite.scala.admin.user.dao.UserDao
import com.qrsof.trackersite.scala.admin.user.pojos.{SignInRequest, SignUpRequest, User}
import com.qrsof.trackersite.scala.admin.utils.TrackerSiteUtils
import jakarta.inject.{Inject, Singleton}
import org.slf4j.{Logger, LoggerFactory}


@Singleton
class UserBusinessImpl @Inject() (
                                 apptackClient: ApptackClient,
                                 jwtValidationService: JwtValidationService,
                                 userDao: UserDao,
                                 trackerSiteUtils: TrackerSiteUtils
                                 ) extends UserBusiness {

  val logger: Logger = LoggerFactory.getLogger(classOf[UserBusinessImpl])


  override def signUpUser(newUser: SignUpRequest): Either[OauthException, JwtToken] = {
    apptackClient.oauth.register(newUser.username, newUser.password) match {
      case Left(_) => Left(UserAlreadyExistsException(newUser.username))
      case Right(registeredUser) =>
        val jwtToken = JwtToken(registeredUser.accessToken)
        val decodedToken: DecodedToken = jwtValidationService.validateJwt(jwtToken).toSeq.head
        logger.debug("decodedToken.subject: {}", decodedToken.subject)
        val key = decodedToken.subject
        val date = trackerSiteUtils.getCurrentDate
        userDao.saveUser(User(key, newUser.email, newUser.name, newUser.lastName, newUser.username, date, date))
        Right(jwtToken)
    }
  }

  override def signInUser(signInRequest: SignInRequest): Either[OauthException, JwtToken] = {
    logger.info("UserBusinessImpl:SignInUser")
    apptackClient.oauth
      .login(signInRequest.username, signInRequest.password) match {
      case Left(_) => Left(OauthUserNotFoundException(signInRequest.username))
      case Right(loggedInUser) => Right(JwtToken(loggedInUser.accessToken))
    }
  }

  override def getDataUserByUserKey(userKey: String): User = {
    logger.info("Get Data User")
    userDao.getUserInfoByUserKey(userKey)
  }


}
