package com.qrsof.trackersite.scala.daos

import com.qrsof.core.database.qrslick.QrPostgresProfile.api._
import com.qrsof.core.database.qrslick.{Pageable, QueryExecutor}
import com.qrsof.trackersite.scala.admin.applications.dao.ApplicationsDao
import com.qrsof.trackersite.scala.admin.applications.pojos.Applications
import com.qrsof.trackersite.scala.admin.pojos.{Pageable => PageableB}
import com.qrsof.trackersite.scala.tables
import com.qrsof.trackersite.scala.tables.ApplicationsTable
import scala.collection.mutable
import jakarta.inject.{Inject, Singleton}

import scala.concurrent.ExecutionContext

@Singleton
class ApplicationsDaoImpl @Inject() (queryExecutor: QueryExecutor)(implicit ex: ExecutionContext) extends ApplicationsDao{

  override def saveNewApp(applications: Applications): Applications = {
    val transactions = mutable.ListBuffer.empty[DBIO[Any]]
    transactions.+=(ApplicationsTable.save(applications))
    val transAppsSeq: Seq[DBIOAction[_, NoStream, _]] = transactions.toSeq
    queryExecutor.syncExecuteQuery(DBIO.seq(transAppsSeq: _*))
    applications
  }

  override def getAppByUserKeyAndName(userKey: String, appName: String): Option[Applications] = {
    queryExecutor.syncExecuteQuery {
      ApplicationsTable.findOptionByProperty(app => app.userKey === userKey && app.name === appName && app.active === true)
    }
  }

  override def getApplicationsByUsr(userKey: String, pageableB: PageableB): Seq[Applications] = queryExecutor.syncExecuteQuery {
    val applications = Seq((applicationTable: ApplicationsTable.EntityTable) => Some(applicationTable.userKey === userKey && applicationTable.active === true))
    ApplicationsTable.findByOptionalCriterias(
      applications,
      _ && _,
      (t: tables.ApplicationsTable.EntityTable) => t.name.asc,
      Pageable(pageableB.pageSize, pageableB.pageNumber)
    )
  }

  override def getApplicationsByName(nameApp: String, userKey: String): Seq[Applications] = {
    val partialName = s"%${nameApp.toLowerCase}%"
    val name = nameApp.toLowerCase
    queryExecutor.syncExecuteQuery {
      ApplicationsTable.findListByProperty { app =>
        (app.userKey === userKey) && (app.name.toLowerCase like partialName) && (app.active === true) || (app.userKey === userKey && app.name.toLowerCase === name) && (app.active === true)
      }
    }
  }

  override def getApplicationByKey(applicationKey: String): Option[Applications] = queryExecutor.syncExecuteQuery{
    ApplicationsTable.findOptionByProperty(_.key === applicationKey)
  }

  override def logicDeleteAppByAppKey(applicationKey: String): String = {
    queryExecutor.syncExecuteQuery {
      ApplicationsTable.findOptionByProperty(_.key === applicationKey)
    } match {
      case Some(app) =>
        val updatedApp = app.copy(active = false)
        queryExecutor.syncExecuteQuery {
          ApplicationsTable.saveOrUpdate(updatedApp)
        }
        applicationKey
      case None =>
        throw new Exception(s"La app con key $applicationKey no se encontró en la base de datos.")
    }
  }

  override def getTotalApplicationsByUsr(userKey: String): Int = {
    val total = queryExecutor.syncExecuteQuery {
      ApplicationsTable.findListByProperty(apps => apps.userKey === userKey && apps.active === true)
    }
    total.length
  }
}
