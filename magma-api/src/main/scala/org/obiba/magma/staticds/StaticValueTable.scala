package org.obiba.magma.staticds

import org.obiba.magma._

class StaticValueTable(var name: String, val datasource: Datasource, val valueType: String, private val entityProvider: EntityProvider)
    extends AbstractValueTable(entityProvider) {

  override def canDrop: Boolean = true

  override def drop(): Unit = {
    // do nothing
  }

  override def getValueSet(entity: Entity): Option[ValueSet] = Some(new ValueSetBean(this, entity))

  override def canDropValueSets: Boolean = false

  override def dropValueSets(): Unit = throw new UnsupportedOperationException("Cannot drop value sets from a static table")

  override def isView: Boolean = false

  override def timestamps: Timestamps = NullTimestamps

}

object StaticValueTable {
  def apply(name: String, datasource: Datasource, valueType: String, entityType: String, entitiesSet: Set[Entity]): StaticValueTable = {
    new StaticValueTable(name, datasource, valueType, new AbstractEntityProvider(entityType) {
      override def entities: Set[Entity] = entitiesSet
    })
  }
}


