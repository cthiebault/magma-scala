package org.obiba.magma

import org.obiba.magma.entity.{Entity, EntityProvider, EntityType}
import org.obiba.magma.value.Value

import scala.collection.SortedSet
import scala.collection.immutable.ListMap

trait ValueTable extends Droppable with Timestamped {

  var name: String

  def datasource: Datasource

  def entityType: EntityType

  def isForEntityType(entityType: String): Boolean

  def entities: Set[Entity]

  def hasEntity(entity: Entity): Boolean

  def entityCount: Int

  def hasValueSet(entity: Entity): Boolean

  def valueSets: Traversable[ValueSet]

  def valueSetCount: Int

  def getValueSet(entity: Entity): Option[ValueSet]

  def canDropValueSets: Boolean

  def dropValueSets(): Unit

  def getValueSetTimestamps(entity: Entity): Option[Timestamps]

  def getValueSetTimestamps(entities: SortedSet[Entity]): Traversable[Timestamps]

  def hasVariable(name: String): Boolean

  def variables: Traversable[Variable]

  def variableCount: Int

  def getVariable(name: String): Option[Variable]

  def getValue(variable: Variable, valueSet: ValueSet): Option[Value]

  def getVariableValueSource(name: String): Option[VariableValueSource]

  def isView: Boolean

  def tableReference: String

}

object ValueTableReference {
  def getReference(datasource: String, table: String): String = {
    s"$datasource.$table"
  }
}

abstract class AbstractValueTable(private val entityProvider: EntityProvider)
  extends ValueTable with Initialisable {

  protected var variableSources: Map[String, VariableValueSource] = ListMap()

  override def isForEntityType(entityType: String): Boolean = entityProvider.isForEntityType(EntityType(entityType))

  override def entityType: EntityType = entityProvider.`type`

  override def entities: Set[Entity] = entityProvider.entities

  override def hasValueSet(entity: Entity): Boolean = entityProvider.entities.contains(entity)

  override def valueSets: Traversable[ValueSet] = {
    entityProvider
      .entities
      .filter(hasValueSet)
      .map(getValueSet(_).get)
  }

  override def variables: Traversable[Variable] = {
    variableSources
      .values
      .map(_.variable)
      .filter(_ != null)
  }

  override def hasVariable(name: String): Boolean = variableSources.contains(name)

  override def getVariable(name: String): Option[Variable] = {
    getVariableValueSource(name) match {
      case Some(s) => Some(s.variable)
      case _ => None
    }
  }

  override def getVariableValueSource(name: String): Option[VariableValueSource] = variableSources.get(name)

  override def hasEntity(entity: Entity): Boolean = entities.contains(entity)

  override def getValue(variable: Variable, valueSet: ValueSet): Option[Value] = {
    getVariableValueSource(variable.name) match {
      case Some(source) => Some(source.getValue(valueSet))
      case _ => None
    }
  }

  override def initialise(): Unit = Initialisable.initialise(variableSources)

  override def getValueSetTimestamps(entity: Entity): Option[Timestamps] = {
    getValueSet(entity) match {
      case Some(vs) => Some(vs.timestamps)
      case _ => None
    }
  }

  override def getValueSetTimestamps(entities: SortedSet[Entity]): Traversable[Timestamps] = {
    entities
      .map(getValueSetTimestamps)
      .filter(_.isDefined)
      .map(_.get)
  }

  override def tableReference: String = {
    ValueTableReference.getReference(if (datasource == null) "null" else datasource.name, name)
  }

  override def valueSetCount: Int = valueSets.size

  override def variableCount: Int = variables.size

  override def entityCount: Int = entities.size

  def canEqual(other: Any): Boolean = other.isInstanceOf[AbstractValueTable]

  override def equals(other: Any): Boolean = other match {
    case that: AbstractValueTable =>
      (that canEqual this) &&
        name == that.name &&
        datasource == that.datasource
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(name, datasource)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def toString = s"$getClass($name, $entityType)"
}
