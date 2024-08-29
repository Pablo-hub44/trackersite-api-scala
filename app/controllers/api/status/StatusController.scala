package controllers.api.status

import controllers.pojos.status.StatusResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.ws.rs.{GET, Path, Produces}
import jakarta.ws.rs.core.MediaType
import play.api.mvc.{Action, AnyContent}

trait StatusController {

  @Path("/public/status")
  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Operation(
    summary = "Get API status",
    tags = Array("Status"),
    responses = Array(
      new ApiResponse(
        responseCode = "200",
        content = Array(
          new Content(
            schema = new Schema(
              implementation = classOf[StatusResponse]
            )
          )
        )
      )
    )
  )
  def status: Action[AnyContent]

}
