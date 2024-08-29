package controllers.api.swagger

import com.qrsof.core.swagger
import com.qrsof.core.swagger.Info
import com.qrsof.core.swagger.SwaggerSupport
import controllers.api.applications.ApplicationController
import controllers.api.recordings.RecordingController
import controllers.api.status.StatusController
import controllers.api.user.UserController
import io.swagger.v3.oas.models.security.{SecurityRequirement, SecurityScheme}

class SwaggerHttpServiceImpl extends SwaggerSupport {
  override def apiClasses: Set[Class[?]] = Set(
    classOf[StatusController],
    classOf[UserController],
    classOf[ApplicationController],
    classOf[RecordingController]
  )

  override val swaggerHost = s"localhost:9000"
  override val swaggerInfo: Info = Info(
    version = "1.0",
    title = "Trackersite API",
    termsOfService = "termsOfService",
    contact = Some(
      swagger.Contact(
        name = "name",
        url = "url",
        email = "email"
      )
    ),
    license = Some(
      swagger.License(
        name = "name",
        url = "url"
      )
    ),
    vendorExtensions = Map(("key", "value"))
  )

  private final val bearer = new SecurityScheme().name("Bearer Security").description("Bearer Token based")
  bearer.setType(SecurityScheme.Type.HTTP)
  bearer.setScheme("Bearer")
  bearer.setBearerFormat("JWT")
  override val swaggerSecuritySchemes: Map[String, SecurityScheme] = Map("Bearer" -> bearer)
  override val swaggerSecurity: List[SecurityRequirement] = List(new SecurityRequirement().addList("Bearer"))

  override val swaggerUnwantedDefinitions: Seq[String] = Seq("Function1", "Function1RequestContextFutureRouteResult")


  override def swaggerSchemes: List[String] = List("http", "https")


}
