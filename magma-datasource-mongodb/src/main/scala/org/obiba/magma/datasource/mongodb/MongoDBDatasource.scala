
package org.obiba.magma.datasource.mongodb

import java.time.{Clock, Instant, LocalDateTime}
import java.util.Locale

import com.mongodb.casbah._
import com.mongodb.casbah.commons.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import org.obiba.magma.attribute.Attribute
import org.obiba.magma.entity.EntityType
import org.obiba.magma.time.{Timestamped, Timestamps, UnionTimestamps}
import org.obiba.magma.{AbstractDatasource, ValueTable, ValueTableWriter}

class MongoDBDatasource(private val mongoDBFactory: MongoDBFactory)(implicit clock: Clock)
  extends AbstractDatasource {

  private val CREATED_FIELD = "created"
  private val UPDATE_FIELD = "update"
  private val TIMESTAMPS_FIELD = "_timestamps"

  private lazy val dBObject: DBObject = {
    datasourceCollection()
      .findOne(MongoDBObject("name" -> name))
      .getOrElse(insert())
  }

  private def insert(): DBObject = {
    val mongoDBObject: DBObject = MongoDBObject(
      "name" -> name,
      TIMESTAMPS_FIELD -> createTimestampsObject()
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
    MongoDBObject(CREATED_FIELD -> now)
  }

  override def `type`: String = "mongodb"

  override def name(): String = dBObject.getAs[String]("name").orNull

  override def name_=(name: String): Unit = {
    dBObject ++
  }

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

  override def timestamps: Timestamps = {
    val list: List[Timestamped] = tables.toList.::(this)
    UnionTimestamps(list)
  }

  private class MongoDBDatasourceTimestamped extends Timestamped {
    override def timestamps: Timestamps = new Timestamps {

      val timestampsObject: DBObject = dBObject.getAs[DBObject](TIMESTAMPS_FIELD).get

      override def created: Instant = timestampsObject.getAs[Instant](CREATED_FIELD).get

      override def lastUpdate: Option[Instant] = timestampsObject.getAs[Instant](UPDATE_FIELD)
    }
  }

}
