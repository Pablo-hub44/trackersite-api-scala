package com.qrsof.trackersite.scala

import com.qrsof.apptack.client.ApptackClientConfigurations
import com.qrsof.trackersite.scala.digitalOcean.DigitalOceanConfigs

trait AppConfigurations {

  def environment: AppEnvironment
  def apiPort: String
  def apiHost: String
  def dbSchemaName: String
  def listenAddresses: String
  def oauthConfigs: OauthConfigs
  def appTackClientConfigurations: ApptackClientConfigurations
  def digitalOceanConfigs: DigitalOceanConfigs
  def fileSystemConfigs: FileSystemConfigs
}
