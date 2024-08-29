package com.qrsof.trackersite.scala

import com.google.inject.{AbstractModule, Provides}
import com.qrsof.apptack.client.ApptackClientConfigurations
import com.qrsof.apptack.client.impl.ApptackModule
import com.qrsof.jwt.validation._
import com.qrsof.libs.storage.digitalocean.StorageDigitalOceanGuiceModule
import com.qrsof.trackersite.scala.AppEnvironment.{Development, Production, Sandbox}
import com.qrsof.trackersite.scala.admin.applications.{ApplicationsBusiness, ApplicationsBusinessImpl}
import com.qrsof.trackersite.scala.admin.recordings.{RecordingsBusiness, RecordingsBusinessImpl}
import com.qrsof.trackersite.scala.admin.test.{TestingBusiness, TestingBusinessImpl}
import com.qrsof.trackersite.scala.admin.user.{UserBusiness, UserBusinessImpl}
import com.qrsof.trackersite.scala.admin.utils.files.digitalOcean.{DigitalOceanResources, DigitalOceanResourcesImpl}
import com.qrsof.trackersite.scala.admin.utils.files.{FilesActions, FilesLocalActionsImpl}
import com.qrsof.trackersite.scala.admin.utils.{TrackerSiteUtils, TrackerSiteUtilsImpl}
import jakarta.inject.Singleton
import net.codingwell.scalaguice.ScalaModule

class AppBusinessModuleGuice(appConfigurations: AppConfigurations) extends AbstractModule with ScalaModule{
  override def configure(): Unit = {
    bind[JwtValidationService].to[JwtValidationServiceImpl].in(classOf[Singleton])
    bind[RSAKeyProvider].to[RSAJwksKeyProvider].in(classOf[Singleton])
    bind[TestingBusiness].to[TestingBusinessImpl].in(classOf[Singleton])
    bind[UserBusiness].to[UserBusinessImpl].in(classOf[Singleton])
    bind[ApplicationsBusiness].to[ApplicationsBusinessImpl].in(classOf[Singleton])
    bind[TrackerSiteUtils].to[TrackerSiteUtilsImpl].in(classOf[Singleton])
    bind[RecordingsBusiness].to[RecordingsBusinessImpl].in(classOf[Singleton])
    install(new ApptackModule)
    install(new StorageDigitalOceanGuiceModule())

    appConfigurations.environment match {
      case Development =>
        bind[FilesActions].to[FilesLocalActionsImpl].in(classOf[Singleton])
        bind[DigitalOceanResources].to[DigitalOceanResourcesImpl].in(classOf[Singleton])
      case Sandbox =>
        bind[FilesActions].to[FilesLocalActionsImpl].in(classOf[Singleton])
        bind[DigitalOceanResources].to[DigitalOceanResourcesImpl].in(classOf[Singleton])
      case Production =>
        bind[FilesActions].to[FilesLocalActionsImpl].in(classOf[Singleton])
        bind[DigitalOceanResources].to[DigitalOceanResourcesImpl].in(classOf[Singleton])
      case _ =>
        bind[FilesActions].to[FilesLocalActionsImpl].in(classOf[Singleton])
        bind[DigitalOceanResources].to[DigitalOceanResourcesImpl].in(classOf[Singleton])
    }
  }

  @Provides
  def jwkConfigs(): JwksConfigs = {
    new JwksConfigs {
      override def jwkUlr: String = appConfigurations.oauthConfigs.jwksUrl
    }
  }

  @Provides
  def apptackClientConfigurations: ApptackClientConfigurations = {
    appConfigurations.appTackClientConfigurations
  }
}
