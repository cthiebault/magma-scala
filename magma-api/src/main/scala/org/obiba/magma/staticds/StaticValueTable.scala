package org.obiba.magma.staticds

import org.obiba.magma._
import org.obiba.magma.value.Value

class StaticValueTable(var name: String, val datasource: Datasource, override val entityType: String, private val entityProvider: EntityProvider)
    extends AbstractValueTable(entityProvider) {
  
  def addValues(identifier: String, variable: Variable, value: Value): Unit = ???

  def removeValues(identifier: String): Unit = ???

  def addEntity(entity: Entity) = ???

  def addVariable(variable: Variable): Unit = ???

  def removeVariable(name: String): Unit = ???

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
  def apply(name: String, datasource: Datasource, entityType: String, entitiesSet: Set[Entity]): StaticValueTable = {
    new StaticValueTable(name, datasource, entityType, new AbstractEntityProvider(entityType) {
      override def entities: Set[Entity] = entitiesSet
    })
  }
}


