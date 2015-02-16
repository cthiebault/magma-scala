package org.obiba.magma.staticds

import org.obiba.magma._
import org.obiba.magma.entity._
import org.obiba.magma.value.{Value, ValueType}

import scala.collection.immutable.ListMap

class StaticValueTable(var name: String, val datasource: Datasource, override val entityType: EntityType,
  private val entityProvider: EntityProvider) extends AbstractValueTable(entityProvider) {

  private var sources: Map[String, VariableValueSource] = ListMap()
  private var values: Map[EntityIdentifier, Map[Variable, Value]] = Map()

  override def canDrop: Boolean = true

  override def drop(): Unit = {
    // do nothing
  }

  override def getValueSet(entity: Entity): Option[ValueSet] = Some(new ValueSetBean(this, entity))

  override def canDropValueSets: Boolean = false

  override def dropValueSets(): Unit = throw new UnsupportedOperationException("Cannot drop value sets from a static table")

  override def isView: Boolean = false

  override def timestamps: Timestamps = NullTimestamps

  def addValues(identifier: EntityIdentifier, variable: Variable, value: Value): Unit = {
    values = values + (identifier -> (values.getOrElse(identifier, Map()) + (variable -> value)))
  }

  def removeValues(identifier: EntityIdentifier): Unit = {
    values = values - identifier
  }

  def addEntity(entity: Entity) = ???

  def addVariable(variable: Variable): Unit = {
    addVariableValueSource(new AbstractVariableValueSource {
      override def variable: Variable = variable

      override def valueType: ValueType = variable.valueType

      override def getValue(valueSet: ValueSet): Value = values.get(valueSet.entity.identifier).get(variable)

      override def supportVectorSource: Boolean = false

      override def asVectorSource: VectorSource = throw new VectorSourceNotSupportedException
    })
  }

  def removeVariable(name: String): Unit = removeVariableValueSource(name)

  protected def addVariableValueSource(source: VariableValueSource) {
    sources = sources + (source.name -> source)
  }

  protected def removeVariableValueSource(variableName: String) {
    sources = sources - variableName
  }

}

object StaticValueTable {
  def apply(name: String, datasource: Datasource, entityType: EntityType, entitiesSet: Set[Entity]): StaticValueTable = {
    new StaticValueTable(name, datasource, entityType, new AbstractEntityProvider(entityType) {
      override def entities: Set[Entity] = entitiesSet
    })
  }
}


