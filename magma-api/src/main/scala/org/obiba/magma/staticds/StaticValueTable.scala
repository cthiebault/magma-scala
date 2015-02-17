package org.obiba.magma.staticds

import org.obiba.magma._
import org.obiba.magma.entity._
import org.obiba.magma.value.{Value, ValueType}

class StaticValueTable(var name: String, val datasource: Datasource, override val entityType: EntityType,
  private val entityProvider: EntityProvider) extends AbstractValueTable(entityProvider) {

  //TODO should we use a value class like VariableName instead of string here?
  private var values: Map[EntityIdentifier, Map[String, Value]] = Map()

  private var _entities: Set[Entity] = Set()

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
    val variableValues = values.getOrElse(identifier, Map()) + (variable.name -> value)
    values = values + (identifier -> variableValues)
  }

  def removeValues(identifier: EntityIdentifier): Unit = {
    values = values - identifier
  }

  def addEntity(entity: Entity) = _entities = _entities + entity

  def addVariable(_variable: Variable): Unit = {
    addVariableValueSource(new AbstractVariableValueSource {
      override def variable: Variable = _variable

      override def valueType: ValueType = _variable.valueType

      override def getValue(valueSet: ValueSet): Value = values.get(valueSet.entity.identifier).get(_variable.name)

      override def supportVectorSource: Boolean = true

      override def asVectorSource: VectorSource = ???
    })
  }

  def removeVariable(name: String): Unit = removeVariableValueSource(name)

  protected def addVariableValueSource(source: VariableValueSource) {
    variableSources = variableSources + (source.name -> source)
  }

  protected def removeVariableValueSource(variableName: String) {
    variableSources = variableSources - variableName
  }

}

object StaticValueTable {
  def apply(name: String, datasource: Datasource, entityType: EntityType, entitiesSet: Set[Entity]): StaticValueTable = {
    new StaticValueTable(name, datasource, entityType, new AbstractEntityProvider(entityType) {
      override def entities: Set[Entity] = entitiesSet
    })
  }

  def apply(name: String, datasource: Datasource, entityType: EntityType): StaticValueTable = {
    apply(name, datasource, entityType, Set())
  }
}


