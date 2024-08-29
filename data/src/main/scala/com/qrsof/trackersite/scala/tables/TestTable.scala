package com.qrsof.trackersite.scala.tables

import com.qrsof.core.database.qrslick.{GenericDao, Lens}
import com.qrsof.core.database.qrslick.QrPostgresProfile.api._
import com.qrsof.trackersite.scala.admin.test.pojos.Tests
import slick.jdbc.JdbcType
import slick.lifted.{ProvenShape, Tag}

class TestTable(tag: Tag) extends Table[Tests](tag, Some("trackersite"), "test") {

  override def * : ProvenShape[Tests] = (testKey, name, description).<>(Tests.apply, Tests.unapply)

  def testKey = column[String]("test_key")
  def name = column[String]("name")
  def description = column[String]("description")



}

object TestTable extends GenericDao {
  override type Entity = Tests
  override type Id = String
  override type EntityTable = TestTable

  override def $id(table: EntityTable): Rep[Id] = table.testKey

  override def idLens: Lens[Entity, Id] = Lens.lens { (entity: Entity) => entity.testKey } { (entity, id) => entity.copy(testKey = id) }

  override def tableQuery: TableQuery[EntityTable] = TableQuery[EntityTable]

  override def baseTypedType = implicitly[JdbcType[Id]]
}
