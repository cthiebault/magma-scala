package org.obiba.magma.datasource.mongodb

import java.time.{Clock, LocalDateTime}
import java.util.Locale

import com.mongodb.casbah.commons.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.{Imports, MongoCollection, WriteConcern}
import org.obiba.magma.attribute.Attribute
import org.obiba.magma.entity.EntityType
import org.obiba.magma.time.Timestamps
import org.obiba.magma.{AbstractDatasource, ValueTable, ValueTableWriter}

class MongoDBDatasource(override var name: String, private val mongoDBFactory: MongoDBFactory)(implicit clock: Clock)
  extends AbstractDatasource {

  private lazy val dBObject: DBObject = {
    datasourceCollection()
      .findOne(MongoDBObject("name" -> name))
      .getOrElse(insert())
  }

  private def insert(): DBObject = {
    val mongoDBObject: DBObject = MongoDBObject(
      "name" -> name,
      "_timestamps" -> createTimestampsObject()
    )
    //TODO ensure index on name exists
    datasourceCollection().insert(mongoDBObject, WriteConcern.Acknowledged)
    mongoDBObject
  }

  private def datasourceCollection(): MongoCollection = {
    mongoDBFactory.execute(new MongoDBCallback[MongoCollection] {
      override def doWithDB(db: Imports.MongoDB): MongoCollection = db("datasource")
    })
  }

  private def createTimestampsObject(): DBObject = {
    val now: LocalDateTime = LocalDateTime.now(clock)
    MongoDBObject("created" -> now, "update" -> now)
  }

  override def `type`: String = "mongodb"

  override def canDropTable(tableName: String): Boolean = ???

  override def tables: Set[ValueTable] = ???

  override def hasEntities(predicate: (ValueTable) => Boolean): Boolean = ???

  override def canRenameTable(tableName: String): Boolean = ???

  override def renameTable(tableName: String, newName: String): Unit = ???

  override def dropTable(tableName: String): Unit = ???

  override def createWriter(tableName: String, entityType: EntityType): ValueTableWriter = ???

  override def addAttribute(attribute: Attribute): Unit = ???

  override def removeAttribute(name: String, namespace: String, locale: Locale): Unit = ???

  override def clearAttributes(): Unit = ???

  override def removeAttributes(name: String): Unit = ???

  override def removeAttributes(name: String, namespace: String): Unit = ???

  override def canDrop: Boolean = true

  override def drop(): Unit = ???

  override def attributes: List[Attribute] = ???

  override def initialise(): Unit = ???

  override def dispose(): Unit = mongoDBFactory.close()

  override def timestamps: Timestamps = ???
}
