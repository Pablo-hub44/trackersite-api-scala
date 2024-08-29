import com.typesafe.sbt.packager.docker.{Cmd, DockerChmodType, ExecCmd}


lazy val scala3 = "3.4.2"

ThisBuild / scalaVersion := scala3
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.qrsof.trackersite"

val AkkaVersion = "2.8.5"
val AkkaHttpVersion = "10.5.3"
val jacksonVersion = "2.17.0"
val swaggerVersion = "2.2.21"
val awsVersion = "1.12.398"

// Docker
enablePlugins(DockerPlugin)
enablePlugins(AshScriptPlugin)

dockerChmodType := DockerChmodType.UserGroupWriteExecute

dockerBaseImage := "docker.io/amazoncorretto:21-alpine"

dockerRepository := Some("417954770034.dkr.ecr.us-west-2.amazonaws.com")

dockerExposedPorts := Seq(80)

dockerExposedVolumes := Seq("/logs", "/conf")

dockerCommands := dockerCommands.value.filterNot {
  case ExecCmd("ENTRYPOINT", args@_*) => true
  case _ => false
}

dockerCommands += Cmd("COPY", "--from=stage0 --chown=demiourgos728:root", "/1/opt/docker/conf/logback-prod.xml", "/conf/logback.xml")

dockerCommands += ExecCmd("ENTRYPOINT", "/opt/docker/bin/trackersite-api", "-Dhttp.port=80", "-Dpidfile.path=/dev/null", "-Dlogger.file=/conf/logback.xml")

// Docker

lazy val commonSettings = Seq(
  resolvers += "Nexus Releases" at "https://nexus-ci.qrsof.com/repository/maven-public",
  credentials += Credentials("Sonatype Nexus Repository Manager", "nexus-ci.qrsof.com", "deployment", "4jDzLGNHgaWiWFj"),
  libraryDependencies ++= commonDependencies,
  coverageExcludedPackages := ".*Module.*;.*Module",
  scalacOptions ++= Seq(
    "-feature",
    "-Wunused:all",
    "-Wshadow:all",
    "-explain",
  ),
  semanticdbEnabled := true,
  semanticdbVersion := scalafixSemanticdb.revision,
  Test / testOptions ++= Seq(
    Tests.Argument(TestFrameworks.ScalaTest, "-o"),
    Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports"),
    Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-reports")
  )
)

lazy val testDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.2.18" % Test,
  "org.scalamock" %% "scalamock" % "6.0.0" % Test,
  "com.vladsch.flexmark" % "flexmark-all" % "0.64.8" % Test
)

lazy val commonDependencies = Seq(
  "com.qrsof.core" %% "crypto" % "2.0.0-01-SNAPSHOT",
  "com.qrsof.core" %% "jwt" % "3.0.0-SNAPSHOT",
  "com.qrsof.core" %% "app-core" % "4.0.0-03-SNAPSHOT",
  "com.qrsof.core" %% "app-play" % "5.0.0-01-SNAPSHOT",
  "com.qrsof.core" %% "app" % "1.0.0-11-SNAPSHOT",
  "com.qrsof.core" %% "certificates" % "5.0.0-01-SNAPSHOT",
  "com.qrsof.core" %% "http-components" % "1.0.0-02-SNAPSHOT",
  "com.qrsof.core.api" %% "swagger" % "1.0.0-01-SNAPSHOT",
  "com.fasterxml.uuid" % "java-uuid-generator" % "5.0.0",
  "org.playframework" %% "play" % "3.0.3",
  "io.scalaland" %% "chimney" % "1.1.0",
  "com.qrsof.apptack" %% "apptack-client" % "3.0.0-02-SNAPSHOT",
  "com.qrsof.core.storage" % "storage-digital-ocean" % "2.0.0-00-SNAPSHOT",
  "com.qrsof.libs.storage" % "storage-filesystem" % "1.0.0-02-SNAPSHOT",
  "com.lihaoyi" %% "ujson" % "3.1.2"
) ++ testDependencies

val swaggerDependencies = Seq(
  "jakarta.ws.rs" % "jakarta.ws.rs-api" % "4.0.0",
  "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-core" % jacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonVersion,
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion,
  "io.swagger.core.v3" % "swagger-jaxrs2-jakarta" % swaggerVersion
)

lazy val business = (project in file("business"))
  .dependsOn()
  .settings(
    libraryDependencies ++= Seq(
      "com.nimbusds" % "nimbus-jose-jwt" % "9.39.1",
      "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
    ),
    Test / testOptions ++= Seq(
      Tests.Argument(TestFrameworks.ScalaTest, "-o"),
      Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-reports"),
      Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports/domain")
    ),
    commonSettings,
  )

lazy val data = (project in file("data"))
  .dependsOn(business)
  .settings(
    libraryDependencies ++= Seq(
      "com.qrsof.core" %% "database-slick-3-5-1" % "2.0.0-01-SNAPSHOT",
      "com.qrsof.core" %% "database-evolution" % "5.0.0-01-SNAPSHOT",
      "com.qrsof.core" %% "database-slick-3-5-1" % "2.0.0-01-SNAPSHOT" % "test" classifier "tests",
    ),
    Test / testOptions ++= Seq(
      Tests.Argument(TestFrameworks.ScalaTest, "-o"),
      Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-reports"),
      Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports/data")
    ),
    commonSettings
  )

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := "trackersite-api",
    commonSettings,
    libraryDependencies ++= Seq(
      guice,
      "net.codingwell" %% "scala-guice" % "6.0.0",
      "org.joda" % "joda-convert" % "2.2.3",
      "net.logstash.logback" % "logstash-logback-encoder" % "7.4",
      "io.lemonlabs" %% "scala-uri" % "4.0.3",
      "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test,
      "ch.qos.logback" % "logback-classic" % "1.5.6"
    )
  )
  .dependsOn(business, data)
  .aggregate(business, data)

import scala.concurrent.duration.*

ThisBuild / forceUpdatePeriod := Some(0.seconds)
