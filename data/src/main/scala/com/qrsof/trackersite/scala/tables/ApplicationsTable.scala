package com.qrsof.trackersite.scala.tables

import com.qrsof.core.database.qrslick.{GenericDao, Lens}
import com.qrsof.core.database.qrslick.QrPostgresProfile.api._
import com.qrsof.trackersite.scala.admin.applications.pojos.Applications
import slick.ast.BaseTypedType
import slick.jdbc.JdbcType
import slick.lifted.{ProvenShape, Tag}

import java.sql.Timestamp
import java.util.Date

class ApplicationsTable(tag: Tag) extends Table[Applications](tag, Some("trackersite"), "applications") {
  implicit val dateMapper: JdbcType[Date] with BaseTypedType[Date] = MappedColumnType.base[Date, Timestamp](
    d => new Timestamp(d.getTime),
    ts => new Date(ts.getTime)
  )

  override def * : ProvenShape[Applications] = (key,  userKey, name, active, createdAt, updatedAt).<>(Applications.apply, Applications.unapply)

  def key = column[String]("key", O.PrimaryKey)
  def userKey = column[String]("user_key")
  def name = column[String]("name")
  def active = column[Boolean]("active")
  def createdAt = column[Date]("created_at")
  def updatedAt = column[Date]("updated_at")

}

object ApplicationsTable extends GenericDao {
  override type Entity = Applications
  override type Id = String
  override type EntityTable = ApplicationsTable

  override def $id(table: EntityTable): Rep[Id] = table.key

  override def idLens: Lens[Entity, Id] = Lens.lens { (entity: Entity) => entity.key } { (entity, id) => entity.copy(key = id) }

  override def tableQuery: TableQuery[EntityTable] = TableQuery[EntityTable]

  override def baseTypedType = implicitly[JdbcType[Id]]
}
