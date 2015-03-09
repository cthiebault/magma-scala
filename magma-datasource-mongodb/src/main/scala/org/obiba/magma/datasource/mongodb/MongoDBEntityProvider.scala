package org.obiba.magma.datasource.mongodb

import java.time.Instant

import com.mongodb.casbah.commons.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import org.obiba.magma.entity._

class MongoDBEntityProvider(val table: MongoDBValueTable, private val _type: EntityType) extends EntityProvider {

  private var lastUpdate: Option[Instant] = None
  private var cachedEntities: Set[Entity] = null

  override def entities: Set[Entity] = {
    if (cachedEntities == null || lastUpdate.isEmpty || lastUpdate.get != table.timestamps.lastUpdate.orNull) {
      lastUpdate = table.timestamps.lastUpdate
      cachedEntities = table.valueSetCollection()
        .find(MongoDBObject(), MongoDBObject("_id" -> 1))
        .map(c => new EntityBean(`type`(), EntityIdentifier(c.getAs[String]("_id").get)))
        .toSet
    }
    cachedEntities
  }

  override def `type`(): EntityType = {
    if (_type == null) table.entityType else _type
  }

  override def isForEntityType(`type`: EntityType): Boolean = `type` == this.`type`

}
