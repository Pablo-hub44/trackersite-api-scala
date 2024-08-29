import com.qrsof.apptack.client.ApptackClientConfigurations
import com.qrsof.trackersite.scala.AppEnvironment.{Development, Production, Sandbox, Test}
import com.qrsof.trackersite.scala.digitalOcean.DigitalOceanConfigs
import com.qrsof.trackersite.scala.{AppConfigurations, AppEnvironment, FileSystemConfigs, OauthConfigs}
import com.typesafe.config.{Config, ConfigFactory}

class AppConfigurationsImpl extends AppConfigurations{

  val config: Config = ConfigFactory.defaultApplication().resolve()

  override def dbSchemaName: String = config.getString("db.schema")

  override def environment: AppEnvironment =
    if (config.hasPath("environment")) {
      val env = config.getString("environment")
      env match {
        case "production" => Production
        case "sandbox" => Sandbox
        case "develop" => Development
        case "test" => Test
        case _ => throw new RuntimeException("Environment not found")
      }
    } else {
      Development
    }

  override def apiHost: String = {
    config.getString("api.host") + ":" + getAppPort
  }

  override def oauthConfigs: OauthConfigs = {
    new OauthConfigs {
      override def jwksUrl: String = config.getString("oauth.jwksUrl")
    }
  }

  override def appTackClientConfigurations: ApptackClientConfigurations = new ApptackClientConfigurations {
    override def url: String = config.getString("apptack.url")

    override def appKey: String = config.getString("apptack.appKey")

    override def appSecret: String = config.getString("apptack.secretKey")
  }

  override def apiPort: String = getAppPort.toString

  private def getAppPort: Int = {
    val envPort = System.getenv("APP_PORT")
    val propPort = System.getProperty("APP_PORT")
    if (envPort != null) {
      envPort.toInt
    } else if (propPort != null) {
      propPort.toInt
    } else {
      9000
    }
  }

  override def listenAddresses: String = {
    val listenAddressEnv = System.getenv("APP_LISTEN_ADDRESS")
    val listenAddressProp = System.getProperty("APP_LISTEN_ADDRESS")
    if (listenAddressEnv != null) {
      listenAddressEnv
    } else if (listenAddressProp != null) {
      listenAddressProp
    } else {
      "0.0.0.0"
    }
  }

  override def digitalOceanConfigs: DigitalOceanConfigs = {
    DigitalOceanConfigs(
      region = config.getString("digitalocean.region"),
      secretKey = config.getString("digitalocean.secretKey"),
      accessKey = config.getString("digitalocean.accessKey"),
      bucketName = config.getString("digitalocean.bucketName"),
      schema = config.getString("db.schema")
    )
  }

  override def fileSystemConfigs: FileSystemConfigs = {
    FileSystemConfigs(
      schema = config.getString("db.schema")
    )
  }
}
