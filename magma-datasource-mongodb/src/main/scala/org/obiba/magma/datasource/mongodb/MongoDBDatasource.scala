
package org.obiba.magma.datasource.mongodb

import java.time.Clock
import java.util.Locale

import com.mongodb.casbah._
import com.mongodb.casbah.commons.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import org.obiba.magma.attribute.Attribute
import org.obiba.magma.entity.EntityType
import org.obiba.magma.logging.Slf4jLogging
import org.obiba.magma.time.{Timestamps, UnionTimestamps}
import org.obiba.magma.{ValueTable, AbstractDatasource, ValueTableWriter}

class MongoDBDatasource(override val name: String, private[mongodb] val mongoDBFactory: MongoDBFactory)(implicit clock: Clock)
  extends AbstractDatasource with Slf4jLogging {

  private val DATASOURCE_COLLECTION = "datasource"
  private val TABLE_COLLECTION = "value_table"

  private val NAME_FIELD = "name"
  private val DATASOURCE_FIELD = "datasource"

  private[mongodb] lazy val dBObject: DBObject = {
    val obj = datasourceCollection()
      .findOne(MongoDBObject(NAME_FIELD -> name))
      .getOrElse(insert())
    logger.debug("dBObject: {}", obj)
    obj
  }

  private def dsTimestamps(): MongoDBTimestamps = new MongoDBTimestamps(dBObject)

  private def insert(): DBObject = {
    val mongoDBObject: DBObject = MongoDBObject(
      NAME_FIELD -> name,
      MongoDBTimestamps.TIMESTAMPS_FIELD -> MongoDBTimestamps.createTimestampsObject()
    )
    //TODO ensure index on name exists
    datasourceCollection().insert(mongoDBObject, WriteConcern.Acknowledged)
    mongoDBObject
  }

  private[mongodb] def datasourceCollection(): MongoCollection = {
    mongoDBFactory.execute(new MongoDBCallback[MongoCollection] {
      override def doWithDB(db: Imports.MongoDB): MongoCollection = db(DATASOURCE_COLLECTION)
    })
  }

  protected[mongodb] def tableCollection(): MongoCollection = {
    //TODO ensure indexes on name & datasource exists
    mongoDBFactory.execute(new MongoDBCallback[MongoCollection] {
      override def doWithDB(db: Imports.MongoDB): MongoCollection = db(TABLE_COLLECTION)
    })
  }

  override def `type`: String = "mongodb"

  override def canDropTable(tableName: String): Boolean = hasTable(tableName)

  override def hasEntities(predicate: (ValueTable) => Boolean): Boolean = ???

  override def canRenameTable(tableName: String): Boolean = hasTable(tableName)

  override def getTable(tableName: String): Option[MongoDBValueTable] = {
    super.getTable(tableName).asInstanceOf[Option[MongoDBValueTable]]
  }

  override def renameTable(tableName: String, newName: String): Unit = {
    if (hasTable(tableName)) {
      getTable(tableName).get.rename(newName)
    }
  }

  override def dropTable(tableName: String): Unit = {
    if (hasTable(tableName)) {
      val table: MongoDBValueTable = getTable(tableName).get
      table.drop()
      removeTable(table)
      setLastUpdate()
    }
  }

  override def createWriter(tableName: String, entityType: EntityType): ValueTableWriter = {
    new MongoDBValueTableWriter(getTable(tableName).getOrElse(createAndAddTable(tableName, entityType)))
  }

  private def createAndAddTable(tableName: String, entityType: EntityType): MongoDBValueTable = {
    val table: MongoDBValueTable = new MongoDBValueTable(tableName, this, entityType)
    addTable(table)
    // TODO we should write table to MongoDB instead of changing DS timestamps
    setLastUpdate()
    table.dBObject
    table
  }

  override def canDrop: Boolean = true

  override def drop(): Unit = {
    valueTableNames().foreach(dropTable)
    datasourceCollection().remove(MongoDBObject("_id" -> dBObject._id))
  }

  override def attributes: List[Attribute] = ???

  override def initialise(): Unit = {
    // mongoDBFactory.mongoClient
    super.initialise()
  }

  override def dispose(): Unit = {
    super.dispose()
    mongoDBFactory.close()
  }

  override protected def valueTableNames(): Set[String] = {
    tableCollection()
      .find(MongoDBObject(DATASOURCE_FIELD -> dBObject._id), MongoDBObject(NAME_FIELD -> 1))
      .map(_.getAs[String](NAME_FIELD).get)
      .toSet
  }

  override def timestamps: Timestamps = {
    new UnionTimestamps(tables.map(_.timestamps).toList.::(dsTimestamps()): _*)
  }

  private def setLastUpdate(): Unit = {
    dsTimestamps().update()
    datasourceCollection().save(dBObject, WriteConcern.Acknowledged)
  }

  override protected def initialiseValueTable(tableName: String): ValueTable = new MongoDBValueTable(tableName, this)

  override def addAttribute(attribute: Attribute): Unit = ???

  override def removeAttribute(name: String, namespace: String, locale: Locale): Unit = ???

  override def clearAttributes(): Unit = ???

  override def removeAttributes(name: String): Unit = ???

  override def removeAttributes(name: String, namespace: String): Unit = ???
}
