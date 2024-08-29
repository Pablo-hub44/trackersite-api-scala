import com.qrsof.trackersite.scala.{AppBusinessModuleGuice, DatabaseModule}
import play.api.ApplicationLoader.Context
import play.api.inject.guice.{GuiceApplicationBuilder, GuiceApplicationLoader}

class MainAppLoader  extends GuiceApplicationLoader {

  override def builder(context: Context): GuiceApplicationBuilder = {

    val configuration = context.initialConfiguration
    val environment = context.environment
    val conf = new AppConfigurationsImpl {}

    initialBuilder
      .in(context.environment)
      .loadConfig(context.initialConfiguration)
      .overrides(
        new Module,
        new ModuleGuice(conf),
        new AppBusinessModuleGuice(conf),
        new DatabaseModule(conf, configuration)
      )
  }
}
