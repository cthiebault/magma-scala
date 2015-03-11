package org.obiba.magma.datasource.staticds

import java.time.Clock

import org.obiba.magma._
import org.obiba.magma.entity._
import org.obiba.magma.time.{Timestamps, TimestampsBean}
import org.obiba.magma.value.{Value, ValueType}

import scala.collection.SortedSet

class StaticValueTable(
  var name: String,
  val datasource: Datasource,
  override val entityType: EntityType,
  protected val entityProvider: EntityProvider)(implicit clock: Clock) extends AbstractValueTable {

  // FIXME entities & EntityProvider.entities are disconnected!

  //TODO should we use a value class like VariableName instead of string here?
  private var values: Map[EntityIdentifier, Map[String, Value]] = Map()

  private var _entities: Set[Entity] = Set()

  private val _timestamps: TimestampsBean = TimestampsBean()

  override def canDrop: Boolean = true

  override def drop(): Unit = {
    // do nothing
  }

  override def getValueSet(entity: Entity): Option[ValueSet] = {
    if (_entities.contains(entity)) Some(new ValueSetBean(this, entity)) else None
  }

  override def canDropValueSets: Boolean = false

  override def dropValueSets(): Unit = throw new UnsupportedOperationException("Cannot drop value sets from a static table")

  override def isView: Boolean = false

  override def timestamps: Timestamps = _timestamps

  def addValues(identifier: EntityIdentifier, variable: Variable, value: Value): Unit = {
    val variableValues = values.getOrElse(identifier, Map()) + (variable.name -> value)
    values = values + (identifier -> variableValues)
    _timestamps.touch()
  }

  def removeValues(identifier: EntityIdentifier): Unit = {
    values = values - identifier
    _entities = _entities.filter(_.identifier != identifier)
    _timestamps.touch()
  }

  override def entities: Set[Entity] = _entities

  override def hasValueSet(entity: Entity): Boolean = entities.contains(entity)

  def addEntity(entity: Entity) = _entities = _entities + entity

  def addVariable(_variable: Variable): Unit = {
    addVariableValueSource(new AbstractVariableValueSource {

      override def variable: Variable = _variable

      override def valueType: ValueType = variable.valueType

      override def getValue(valueSet: ValueSet): Value = values.get(valueSet.entity.identifier).get(variable.name)

      override def supportVectorSource: Boolean = true

      override def asVectorSource: VectorSource = new VectorSource {

        override def valueType: ValueType = variable.valueType

        override def getValues(entities: SortedSet[Entity]): Traversable[Value] = {
          values.values
            .map(_.get(variable.name)) // Iterable[Option[Value]]
            .map(_.getOrElse(valueType.nullValue))
        }
      }
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
  def apply(name: String, datasource: Datasource, entityType: EntityType, entitiesSet: Set[Entity])
    (implicit clock: Clock): StaticValueTable = {
    new StaticValueTable(name, datasource, entityType, new AbstractEntityProvider(entityType) {
      override def entities: Set[Entity] = entitiesSet
    })
  }

  def apply(name: String, datasource: Datasource, entityType: EntityType)(implicit clock: Clock): StaticValueTable = {
    apply(name, datasource, entityType, Set())
  }
}


