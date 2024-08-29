package controllers.api.user

import com.qrsof.trackersite.scala.admin.user.pojos.{SignInResponse, SignUpResponse, User}
import controllers.api.ApIError
import controllers.pojos.users.{SignInForm, SignUpForm}
import jakarta.ws.rs._
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.ws.rs.core.MediaType
import play.api.mvc.{Action, AnyContent}

trait UserController {

  @Path("/oauth/signup")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(
    summary = "Add new user",
    tags = Array("Users"),
    requestBody = new RequestBody(
      required = true,
      content = Array(
        new Content(
          schema = new Schema(implementation = classOf[SignUpForm])
        )
      )
    ),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Singup an user session", content = Array(new Content(schema = new Schema(implementation = classOf[SignUpResponse])))),
      new ApiResponse(responseCode = "401", description = "Not authorized", content = Array(new Content(schema = new Schema(implementation = classOf[ApIError])))),
    )
  )
  def signUpUser: Action[AnyContent]

  @Path("/oauth/signin")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(
    summary = "Login user",
    tags = Array("Users"),
    requestBody = new RequestBody(
      required = true,
      content = Array(
        new Content(
          schema = new Schema(implementation = classOf[SignInForm])
        )
      )
    ),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Sing in an user session", content = Array(new Content(schema = new Schema(implementation = classOf[SignInResponse])))),
      new ApiResponse(responseCode = "400", description = "Bad request", content = Array(new Content(schema = new Schema(implementation = classOf[ApIError])))),
      new ApiResponse(responseCode = "404", description = "User not found", content = Array(new Content(schema = new Schema(implementation = classOf[ApIError])))),
    )
  )
  def signInUser(): Action[AnyContent]

  @Path("/private/user/data")
  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(
    summary = "Get User Data",
    tags = Array("Users"),
    security = Array(
      new SecurityRequirement(
        name = "Bearer"
      )
    ),
    responses = Array(
      new ApiResponse(
        responseCode = "200",
        content = Array(
          new Content(
            schema = new Schema(
              implementation = classOf[User]
            )
          )
        )
      ),
      new ApiResponse(
        responseCode = "401",
        content = Array(
          new Content(
            schema = new Schema(implementation = classOf[ApIError])
          )
        )
      ),
      new ApiResponse(
        responseCode = "404",
        content = Array(
          new Content(
            schema = new Schema(implementation = classOf[ApIError])
          )
        )
      )
    )
  )
  def getUserData: Action[AnyContent]
}
