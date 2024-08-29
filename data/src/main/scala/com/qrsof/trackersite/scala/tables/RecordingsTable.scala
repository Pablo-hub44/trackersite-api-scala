package com.qrsof.trackersite.scala.tables

import com.qrsof.core.database.qrslick.{GenericDao, Lens}
import com.qrsof.core.database.qrslick.QrPostgresProfile.api._
import com.qrsof.trackersite.scala.admin.recordings.pojos.Recordings
import slick.ast.BaseTypedType
import slick.jdbc.JdbcType
import slick.lifted.{ProvenShape, Tag}

import java.sql.{Date, Timestamp}

class RecordingsTable(tag: Tag) extends Table[Recordings](tag, Some("trackersite"), "recordings") {
  implicit val dateMapper: JdbcType[Date] with BaseTypedType[Date] = MappedColumnType.base[Date, Timestamp](
    d => new Timestamp(d.getTime),
    ts => new Date(ts.getTime)
  )

  override def * : ProvenShape[Recordings] = (key, eventsReference, createdAt, updatedAt, applicationKey).<>(Recordings.apply, Recordings.unapply)

  def key = column[String]("key", O.PrimaryKey)

  def eventsReference = column[String]("events_reference")

  def createdAt = column[Timestamp]("created_at")

  def updatedAt = column[Timestamp]("updated_at")

  def applicationKey = column[String]("application_key")
}

  object RecordingsTable extends GenericDao {
    override type Entity = Recordings
    override type Id = String
    override type EntityTable = RecordingsTable

    override def $id(table: EntityTable): Rep[Id] = table.key

    override def idLens: Lens[Entity, Id] = Lens.lens { (entity: Entity) => entity.key } { (entity, id) => entity.copy(key = id) }

    override def tableQuery: TableQuery[EntityTable] = TableQuery[EntityTable]

    override def baseTypedType = implicitly[JdbcType[Id]]

  }
