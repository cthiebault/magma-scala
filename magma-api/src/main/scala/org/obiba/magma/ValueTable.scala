package org.obiba.magma

import org.obiba.magma.value.Value

import scala.collection.SortedSet

trait ValueTable extends Droppable {

  var name: String

  def datasource: Datasource

  def entityType: String

  def entities: Set[Entity]

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

  def getValue(variable: Variable, valueSet: ValueSet): Value

  def getVariableValueSource(variableName: String): Option[VariableValueSource]

  def isView: Boolean

  def tableReference: String

  object Reference {
    def getReference(datasource: String, table: String): String = {
      datasource + "." + table
    }
  }

}

abstract class AbstractValueTable extends ValueTable {

}
