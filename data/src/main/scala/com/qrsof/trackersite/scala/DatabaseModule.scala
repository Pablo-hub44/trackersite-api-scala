package com.qrsof.trackersite.scala

import com.google.inject.{AbstractModule, Provides}
import com.qrsof.core.evolution.database.{DatabaseEvolutionModule, QrEvolutionResources}
import com.qrsof.trackersite.scala.daos.{ApplicationsDaoImpl, CustomersDaoImpl, RecordingsDaoImpl, TestingDaoImpl, UserDaoImpl}
import jakarta.inject.Singleton
import net.codingwell.scalaguice.ScalaModule
import org.hibernate.cfg.JdbcSettings
import com.qrsof.core.database.qrslick.QrPostgresProfile.api._
import com.qrsof.libs.storage.digitalocean.StorageConfig
import com.qrsof.trackersite.scala.AppEnvironment.Test
import com.qrsof.trackersite.scala.admin.applications.dao.ApplicationsDao
import com.qrsof.trackersite.scala.admin.customers.dao.CustomersDao
import com.qrsof.trackersite.scala.admin.recordings.dao.RecordingsDao
import com.qrsof.trackersite.scala.admin.test.dao.TestsDao
import com.qrsof.trackersite.scala.admin.user.dao.UserDao
import jakarta.inject.Named
import play.api.Configuration
import slick.jdbc.hikaricp.HikariCPJdbcDataSource

import java.sql.Connection
import java.util.Properties

class DatabaseModule(appConfigurations: AppConfigurations, configurations: Configuration) extends AbstractModule with ScalaModule{

  override def configure(): Unit = {
    if (!appConfigurations.environment.equals(Test)) {
      install(new DatabaseEvolutionModule())
    }
    bind[TestsDao].to[TestingDaoImpl].in(classOf[Singleton])
    bind[UserDao].to[UserDaoImpl].in(classOf[Singleton])
    bind[ApplicationsDao].to[ApplicationsDaoImpl].in(classOf[Singleton])
    bind[RecordingsDao].to[RecordingsDaoImpl].in(classOf[Singleton])
    bind[CustomersDao].to[CustomersDaoImpl].in(classOf[Singleton])
  }

  @Provides
  def qrEvolutionResources(database: Database): QrEvolutionResources = {
    new QrEvolutionResources {
      override def context: Seq[String] = Seq(appConfigurations.environment.entryName.toLowerCase)

      override def changeLog: String = "liquibase/master.yaml"

      override def schema: Option[String] = Some("trackersite")

      override def connection: Connection = database.source.createConnection()
    }
  }

  @Provides
  def getDatabase: Database = {
    if (appConfigurations.environment.equals(Test)) {
      Database.forConfig("")
    } else {
      Database.forConfig("db.postgres")
    }
  }

  @Provides
  @Singleton
  @Named("jdbc-properties") def getSessionFactory(database: Database): Properties = {

    val schemaName = configurations.get[String]("db.schema")
    val source = database.source
    val dataSource = source.asInstanceOf[HikariCPJdbcDataSource]

    val properties = new Properties
    properties.put(JdbcSettings.JAKARTA_JTA_DATASOURCE, dataSource.ds)
    properties.put("hibernate.default_schema", schemaName)
    properties.put("hibernate.show_sql", true)
    properties.put("hibernate.format_sql", true)
    properties.put("hibernate.highlight_sql", true)
    properties
  }

  @Provides
  def storageConfig(databaseApp: Database): StorageConfig = {
    new StorageConfig(
      appConfigurations.digitalOceanConfigs.region,
      appConfigurations.digitalOceanConfigs.secretKey,
      appConfigurations.digitalOceanConfigs.accessKey,
      appConfigurations.digitalOceanConfigs.bucketName,
      databaseApp.source.createConnection(),
      appConfigurations.digitalOceanConfigs.schema
    )
  }

}
