package org.obiba.magma.datasource.mongodb

import org.obiba.magma.entity.Entity
import org.obiba.magma.value.Value
import org.obiba.magma.{ValueTableWriter, Variable}

class MongoDBValueTableWriter(private val table: MongoDBValueTable) extends ValueTableWriter {

  override def writeVariables: VariableWriter = new MongoDBVariableWriter

  override def writeValueSet(entity: Entity): ValueSetWriter = new MongoDBValueSetWriter(entity)

  override def close(): Unit = table.updateLastUpdate()

  class MongoDBVariableWriter extends VariableWriter {

    override def writeVariable(variable: Variable): Unit = ???

    override def removeVariable(variable: Variable): Unit = ???

    override def close(): Unit = ???
  }

  class MongoDBValueSetWriter(private val entity: Entity) extends ValueSetWriter {

    override def writeValue(variable: Variable, value: Value): Unit = ???

    override def remove(): Unit = ???

    override def close(): Unit = ???
  }

}

