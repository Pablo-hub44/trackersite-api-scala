package com.qrsof.trackersite.scala.tables

import com.qrsof.core.database.qrslick.{GenericDao, Lens}
import com.qrsof.core.database.qrslick.QrPostgresProfile.api._
import com.qrsof.trackersite.scala.admin.user.pojos.User
import slick.ast.BaseTypedType
import slick.jdbc.JdbcType
import slick.lifted.{ProvenShape, Tag}

import java.sql.Timestamp
import java.util.Date

class UsersTable(tag: Tag) extends Table[User](tag, Some("trackersite"), "users") {
  implicit val dateMapper: JdbcType[Date] with BaseTypedType[Date] = MappedColumnType.base[Date, Timestamp](
    d => new Timestamp(d.getTime),
    ts => new Date(ts.getTime)
  )

  override def * : ProvenShape[User] = (key, email, name, last_name, username, createdAt, updatedAt).<>(User.apply, User.unapply)

  def key = column[String]("key", O.PrimaryKey)
  def email = column[String]("email")
  def name = column[String]("name")
  def last_name = column[String]("last_name")
  def username = column[String]("username")
  def createdAt = column[Date]("created_at")
  def updatedAt = column[Date]("updated_at")


  //  def idx_user = index("idx_user", (userKey, email), unique = true)

}

object UsersTable extends GenericDao {
  override type Entity = User
  override type Id = String
  override type EntityTable = UsersTable

  override def $id(table: EntityTable): Rep[Id] = table.key

  override def idLens: Lens[Entity, Id] = Lens.lens { (entity: Entity) => entity.key } { (entity, id) => entity.copy(key = id) }

  override def tableQuery: TableQuery[EntityTable] = TableQuery[EntityTable]

  override def baseTypedType = implicitly[JdbcType[Id]]
}
