package controllers.api.applications

import controllers.api.ApIError
import controllers.pojos.applications.{AddApplicationForm, ApplicationResponse, ApplicationsResponse}
import io.swagger.v3.oas.annotations.{Operation, Parameter}
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.ws.rs.{Consumes, GET, POST, PUT, Path, PathParam, Produces}
import jakarta.ws.rs.core.MediaType
import play.api.mvc.{Action, AnyContent}

trait ApplicationController {

  @Path("/private/applications")
  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(
    summary = "Get Applications with User Key",
    tags = Array("Applications"),
    security = Array(
      new SecurityRequirement(
        name = "Bearer"
      )
    ),
    parameters = Array(
      new Parameter(
        name = "page",
        in = ParameterIn.QUERY,
        required = true,
        description = "Number of page",
        content = Array(
          new Content(schema = new Schema(implementation = classOf[Int]))
        )
      ),
      new Parameter(
        name = "pageSize",
        in = ParameterIn.QUERY,
        required = true,
        description = "Number of results per page",
        content = Array(
          new Content(schema = new Schema(implementation = classOf[Int]))
        )
      )
    ),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Get Apps by User", content = Array(new Content(schema = new Schema(implementation = classOf[ApplicationsResponse])))),
      new ApiResponse(responseCode = "401", description = "User Inauthorized", content = Array(new Content(schema = new Schema(implementation = classOf[ApIError])))),
      new ApiResponse(responseCode = "404", description = "Apps not found", content = Array(new Content(schema = new Schema(implementation = classOf[ApIError])))),
    )  )
  def getApplicationsByUsrKey(page: Int, pageSize: Int): Action[AnyContent]

  @Path("/private/applications/application")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(
    summary = "Add new application",
    tags = Array("Applications"),
    security = Array(
      new SecurityRequirement(
        name = "Bearer"
      )
    ),
    requestBody = new RequestBody(
      required = true,
      content = Array(
        new Content(
          schema = new Schema(implementation = classOf[AddApplicationForm])
        )
      )
    ),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Add new App", content = Array(new Content(schema = new Schema(implementation = classOf[ApplicationResponse])))),
      new ApiResponse(responseCode = "500", description = "Internal Server Error", content = Array(new Content(schema = new Schema(implementation = classOf[ApIError])))),
      new ApiResponse(responseCode = "401", description = "User Inauthorized", content = Array(new Content(schema = new Schema(implementation = classOf[ApIError])))),
    )  )
  def addNewApplication(): Action[AnyContent]

  @Path("/private/applications/search/{nameApp}")
  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(
    summary = "Get Applications by WordFilter",
    tags = Array("Applications"),
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
            )
          )
        )
      ),
      new ApiResponse(responseCode = "404", description = "Apps not found", content = Array(new Content(schema = new Schema(implementation = classOf[ApIError])))),
    )
  )
  def getApplicationsByWordFilter(@PathParam("nameApp") nameApp: String) : Action[AnyContent]

  @Path("/private/applications/application/{applicationKey}")
  @PUT
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(
    summary = "Logic Delete Application",
    tags = Array("Applications"),
    security = Array(
      new SecurityRequirement(
        name = "Bearer"
      )
    ),
    responses = Array(
      new ApiResponse(responseCode = "200", description = "Logic Delete App", content = Array(new Content(schema = new Schema(implementation = classOf[ApplicationResponse])))),
      new ApiResponse(responseCode = "401", description = "User Inauthorized", content = Array(new Content(schema = new Schema(implementation = classOf[ApIError])))),
      new ApiResponse(responseCode = "404", description = "Apps not found", content = Array(new Content(schema = new Schema(implementation = classOf[ApIError])))),
    )
  )
  def logicDeleteApplication(@PathParam("applicationKey") applicationKey: String): Action[AnyContent]

  @Path("/private/applications/application/{applicationKey}")
  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(
    summary = "Get Application by Key",
    tags = Array("Applications"),
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
            )
          )
        )
      ),
      new ApiResponse(responseCode = "404", description = "Apps not found", content = Array(new Content(schema = new Schema(implementation = classOf[ApIError])))),
    )
  )
  def getApplicationByKey(@PathParam("applicationKey") applicationKey: String): Action[AnyContent]
}
