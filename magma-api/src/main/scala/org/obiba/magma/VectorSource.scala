package org.obiba.magma

import org.obiba.magma.value.{ValueType, Value}

import scala.collection.SortedSet

trait VectorSource {

  def valueType: ValueType

  def getValues(entities: SortedSet[Entity]): Traversable[Value]

}