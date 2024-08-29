import com.google.inject.AbstractModule
import com.qrsof.trackersite.scala.AppConfigurations
import controllers.api.applications.{ApplicationController, ApplicationControllerImpl}
import controllers.api.recordings.{RecordingController, RecordingControllerImpl}
import controllers.api.status.{StatusController, StatusControllerImpl}
import controllers.api.swagger.{ApiSwaggerController, ApiSwaggerControllerImpl}
import controllers.api.user.{UserController, UserControllerImpl}
import jakarta.inject.Singleton
import net.codingwell.scalaguice.ScalaModule
import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.actor.typed.scaladsl.Behaviors


class ModuleGuice(appConfigurations: AppConfigurations) extends AbstractModule with ScalaModule {

  private val actorSystem: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "apptack-api")

  override def configure(): Unit = {
    bind[ActorSystem[Nothing]].toInstance(actorSystem)
    bind[StatusController].to[StatusControllerImpl].in(classOf[Singleton])
    bind[UserController].to[UserControllerImpl].in(classOf[Singleton])
    bind[ApplicationController].to[ApplicationControllerImpl].in(classOf[Singleton])
    bind[RecordingController].to[RecordingControllerImpl].in(classOf[Singleton])
    bind[ApiSwaggerController].to[ApiSwaggerControllerImpl].in(classOf[Singleton])
    bind[AppConfigurations].toInstance(appConfigurations)
  }
}
