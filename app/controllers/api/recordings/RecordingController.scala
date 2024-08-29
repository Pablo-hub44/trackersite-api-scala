package controllers.api.recordings

import controllers.api.ApIError
import controllers.pojos.recordings.{AddRecordingForm, RecordingResponse, RecordingsResponse}
import io.swagger.v3.oas.annotations.{Operation, Parameter}
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.ws.rs.{Consumes, GET, POST, Path, PathParam, Produces}
import jakarta.ws.rs.core.MediaType
import play.api.mvc.{Action, AnyContent}

trait RecordingController {

  @Path("/private/recordings/application/{appKey}")
  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(
    summary = "Get recordings by appKey and userKey",
    tags = Array("Recordings"),
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
      new ApiResponse(
        responseCode = "200",
        content = Array(
          new Content(
            schema = new Schema(
              implementation = classOf[RecordingsResponse]
            )
          )
        )
      ),
      new ApiResponse(responseCode = "401", description = "User inauthorized", content = Array(new Content(schema = new Schema(implementation = classOf[ApIError])))),
      new ApiResponse(responseCode = "404", description = "Recordings not found", content = Array(new Content(schema = new Schema(implementation = classOf[ApIError])))),
    )
  )
  def getRecordingsByAppKey(@PathParam("appKey") appKey: String, page: Int, pageSize: Int,
                            startDate: Option[String], endDate: Option[String]): Action[AnyContent]

  @Path("/private/recordings/recording/{recordingKey}")
  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(
    summary = "Get Recording by key",
    tags = Array("Recordings"),
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
      new ApiResponse(responseCode = "404", description = "Recording not found", content = Array(new Content(schema = new Schema(implementation = classOf[ApIError])))),
    )
  )
  def getRecordingByKey(@PathParam("recordingKey") appKey: String): Action[AnyContent]

  @Path("/recordings/recording")
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(
    summary = "Add new recording into app",
    tags = Array("Recordings"),
    requestBody = new RequestBody(
      required = true,
      content = Array(
        new Content(
          schema = new Schema(implementation = classOf[AddRecordingForm])
        )
      )
    ),
    responses = Array(
      new ApiResponse(
        responseCode = "200",
        content = Array(
          new Content(
            schema = new Schema(
              implementation = classOf[RecordingResponse]
            )
          )
        )
      ),
      new ApiResponse(responseCode = "400", description = "Bad Request", content = Array(new Content(schema = new Schema(implementation = classOf[ApIError])))),
    )
  )
  def addNewRecordingIntoApp(): Action[AnyContent]

}
