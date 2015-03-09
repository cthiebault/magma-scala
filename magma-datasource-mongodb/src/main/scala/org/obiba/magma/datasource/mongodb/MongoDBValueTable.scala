package org.obiba.magma.datasource.mongodb

import java.time.Clock

import com.mongodb.casbah.commons.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.{Imports, MongoCollection, WriteConcern}
import org.obiba.magma.entity.{Entity, EntityProvider, EntityType}
import org.obiba.magma.time.Timestamps
import org.obiba.magma.{AbstractValueTable, ValueSet}

class MongoDBValueTable(
  var name: String,
  val datasource: MongoDBDatasource,
  private var entityProvider: EntityProvider)(implicit clock: Clock) extends AbstractValueTable(entityProvider) {

  private val DATASOURCE_FIELD = "datasource"
  private val NAME_FIELD = "name"
  private val ENTITY_TYPE_FIELD = "entityType"

  private val VALUE_SET_SUFFIX = "_value_set"
  private val VARIABLE_SUFFIX = "_variable"

  private val dBObject: DBObject = {
    datasource.tableCollection()
      .findOne(MongoDBObject(DATASOURCE_FIELD -> datasource.dBObject._id, NAME_FIELD -> name))
      .getOrElse(insert())
  }

  private def insert(): DBObject = {
    val mongoDBObject: DBObject = MongoDBObject(
      DATASOURCE_FIELD -> datasource.dBObject._id,
      NAME_FIELD -> name,
      ENTITY_TYPE_FIELD -> entityType,
      MongoDBTimestamps.TIMESTAMPS_FIELD -> MongoDBTimestamps.createTimestampsObject()
    )
    datasource.tableCollection().insert(mongoDBObject, WriteConcern.Acknowledged)
    mongoDBObject
  }

  override def getValueSet(entity: Entity): Option[ValueSet] = ???

  override def canDropValueSets: Boolean = ???

  override def dropValueSets(): Unit = ???

  override def isView: Boolean = ???

  override def canDrop: Boolean = ???

  override def drop(): Unit = ???

  override def timestamps: MongoDBTimestamps = new MongoDBTimestamps(dBObject)

  protected[mongodb] def variableCollection(): MongoCollection = {
    datasource.mongoDBFactory.execute(new MongoDBCallback[MongoCollection] {
      //TODO ensure index on name  exists
      override def doWithDB(db: Imports.MongoDB): MongoCollection = db(dBObject._id + VARIABLE_SUFFIX)
    })
  }

  protected[mongodb] def valueSetCollection(): MongoCollection = {
    datasource.mongoDBFactory.execute(new MongoDBCallback[MongoCollection] {
      override def doWithDB(db: Imports.MongoDB): MongoCollection = db(dBObject._id + VALUE_SET_SUFFIX)
    })
  }

  protected[mongodb] def rename(newName: String) = {
    name = newName
    dBObject += NAME_FIELD -> newName
    updateLastUpdate()
  }

  protected[mongodb] def updateLastUpdate() = {
    timestamps.update()
    datasource.tableCollection().save(dBObject, WriteConcern.Acknowledged)
  }


}

object MongoDBValueTable {

  def apply(name: String, datasource: MongoDBDatasource)(implicit clock: Clock): MongoDBValueTable = {
    val table: MongoDBValueTable = new MongoDBValueTable(name, datasource, null)
    table.entityProvider = new MongoDBEntityProvider(table, null.asInstanceOf[EntityType])
    table
  }

}
