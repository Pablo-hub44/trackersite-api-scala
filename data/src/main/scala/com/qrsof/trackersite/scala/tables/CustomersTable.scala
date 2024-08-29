package com.qrsof.trackersite.scala.tables

import com.qrsof.core.database.qrslick.{GenericDao, Lens}
import com.qrsof.core.database.qrslick.QrPostgresProfile.api._
import com.qrsof.trackersite.scala.admin.customers.pojos.Customers
import slick.ast.BaseTypedType
import slick.jdbc.JdbcType
import slick.lifted.{ProvenShape, Tag}

import java.sql.Timestamp
import java.util.Date

class CustomersTable(tag: Tag) extends Table[Customers] (tag, Some("trackersite"), "customers"){
  implicit val dateMapper: JdbcType[Date] with BaseTypedType[Date] = MappedColumnType.base[Date, Timestamp](
    d => new Timestamp(d.getTime),
    ts => new Date(ts.getTime)
  )

  override def * : ProvenShape[Customers] = (key, active, createdAt, updatedAt, userKey).<>(Customers.apply, Customers.unapply)

  def key = column[String] ("key", O.PrimaryKey)

  def active = column[Boolean]("active")

  def createdAt = column[Date]("created_at")

  def updatedAt = column[Date]("updated_at")

  def userKey = column[String]("user_key")

}

object CustomersTable extends GenericDao {
  override type Entity = Customers
  override type Id = String
  override type EntityTable = CustomersTable

  override def $id(table: CustomersTable): Rep[Id] = table.key

  override def idLens: Lens[Entity, Id] = Lens.lens { (entity: Entity) => entity.key } { (entity, id) => entity.copy(key = id) }

  override def tableQuery: TableQuery[EntityTable] = TableQuery[EntityTable]

  override def baseTypedType = implicitly[JdbcType[Id]]
}
