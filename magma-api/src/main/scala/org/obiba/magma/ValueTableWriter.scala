package org.obiba.magma

import org.obiba.magma.value._

trait ValueTableWriter extends AutoCloseable {

  def writeVariables: VariableWriter

  def writeValueSet(entity: Entity): ValueSetWriter

  def close(): Unit

  trait VariableWriter extends AutoCloseable {
    
    def writeVariable(variable: Variable)

    def removeVariable(variable: Variable)

    def close(): Unit

  }

  trait ValueSetWriter extends AutoCloseable {
    
    def writeValue(variable: Variable, value: Value)

    def remove(): Unit

    def close(): Unit

  }

}
