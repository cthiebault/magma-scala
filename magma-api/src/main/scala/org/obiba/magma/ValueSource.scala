package org.obiba.magma

import org.obiba.magma.value.{ValueType, Value}


/**
 * Defines the contract for obtaining a particular {@link Value} from a {@code ValueSet}.
 */
trait ValueSource {

  def valueType: ValueType

  /**
   * This method should never return null.
   */
  def getValue(valueSet: ValueSet): Value

  def supportVectorSource: Boolean

  @throws(classOf[VectorSourceNotSupportedException])
  def asVectorSource: VectorSource

}

/**
 * Defines the contract for obtaining a {@link Value} of a specific {@link Variable} within a {@link ValueSet}
 */
trait VariableValueSource extends ValueSource {

  def name: String

  def variable: Variable
}

trait VariableValueSourceFactory {
  def createSources: Set[VariableValueSource]
}

trait ValueSourceWriter extends ValueSource {

  def addVariableValueSources(factory: VariableValueSourceFactory): Unit

  def addVariableValueSources(sources: Traversable[VariableValueSource]): Unit

  def addVariableValueSource(source: VariableValueSource): Unit

  def removeVariableValueSource(variableName: String): Unit

  def removeVariableValueSources(sources: Iterable[VariableValueSource]): Unit

}