package org.obiba.magma.static

import org.obiba.magma._
import org.obiba.magma.attribute.ListAttributeWriter
import org.obiba.magma.entity.Entity
import org.obiba.magma.staticds.StaticValueTable
import org.obiba.magma.value.Value

class StaticDatasource(override var name: String) extends AbstractDatasource with ListAttributeWriter {

  private var _tables: Map[String, ValueTable] = Map()

  override def `type`: String = "static"

  override def tables: Set[ValueTable] = _tables.values.toSet

  override def getTable(tableName: String): Option[ValueTable] = _tables.get(tableName)

  override def canDropTable(tableName: String): Boolean = hasTable(tableName)

  override def dropTable(tableName: String): Unit = {
    val table: ValueTable = getTable(tableName).getOrElse(throw new NoSuchElementException)
    table.drop()
    _tables = _tables - tableName
  }

  override def canDrop: Boolean = true

  override def drop(): Unit = {
    _tables = Map()
    clearAttributes()
  }

  override def canRenameTable(tableName: String): Boolean = hasTable(tableName)

  override def renameTable(tableName: String, newName: String): Unit = {
    val table: ValueTable = getTable(tableName).getOrElse(throw new NoSuchElementException)
    table.name = newName
    _tables = _tables + (newName -> table)
  }

  override def hasEntities(predicate: ValueTable => Boolean): Boolean = ???

  override def createWriter(tableName: String, entityType: String): ValueTableWriter = ???

  override def initialise(): Unit = {}

  private class StaticValueTableWriter(val table: StaticValueTable) extends ValueTableWriter {

    override def writeVariables: VariableWriter = {
      new VariableWriter {
        override def writeVariable(variable: Variable): Unit = table.addVariable(variable)

        override def removeVariable(variable: Variable): Unit = table.removeVariable(variable.name)

        override def close(): Unit = {}
      }
    }

    override def writeValueSet(entity: Entity): ValueSetWriter = {
      if (!table.hasEntity(entity)) {
        table.addEntity(entity)
      }
      new ValueSetWriter {
        override def close(): Unit = {}

        override def writeValue(variable: Variable, value: Value): Unit = {
          table.addValues(entity.identifier, variable, value)
        }

        override def remove(): Unit = {
          table.removeValues(entity.identifier)
        }
      }
    }

    override def close(): Unit = {}

  }

}

