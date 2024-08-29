package controllers.api.user

import com.qrsof.core.app.api.play.BasePlayController
import com.qrsof.core.app.api.play.security.AuthenticatedAction
import com.qrsof.trackersite.scala.admin.user.OauthExceptionObj.{OauthUserNotFoundException, UserAlreadyExistsException}
import com.qrsof.trackersite.scala.admin.user.UserBusiness
import com.qrsof.trackersite.scala.admin.user.pojos.{SignInRequest, SignUpRequest, User}
import controllers.pojos.users.{SignInForm, SignUpForm}
import io.scalaland.chimney.dsl._
import play.api.mvc._
import jakarta.inject.{Inject, Singleton}


@Singleton
class UserControllerImpl @Inject()(cc: ControllerComponents,
                                   userBusiness: UserBusiness,
                                   authenticatedAction: AuthenticatedAction,
                                   ) extends AbstractController(cc) with UserController with BasePlayController {

  def signInUser(): Action[AnyContent] = Action { implicit request =>
    val userSignInForm = bindFromRequest[SignInForm]().get
    userBusiness.signInUser(userSignInForm.transformInto[SignInRequest]) match {
      case Left(error) =>
        error match {
          case OauthUserNotFoundException(username) =>
            BadRequest(username.toJsValue)
        }
      case Right(success) =>
        Ok(success.toJsValue)
    }
  }

  def signUpUser: Action[AnyContent] = Action { implicit request =>
    val userSignUp = bindFromRequest[SignUpForm]().get
    userBusiness.signUpUser(userSignUp.transformInto[SignUpRequest]) match {
      case Left(error) => error match {
        case UserAlreadyExistsException(username) => Conflict(username.toJsValue)
      }
      case Right(success) =>
        Ok(success.toJsValue)
    }
  }

  def getUserData: Action[AnyContent] = authenticatedAction { implicit request =>
    val userKey = request.principal
    val userData: User = userBusiness.getDataUserByUserKey(userKey)
    Ok(userData.toJsValue)
  }
}
