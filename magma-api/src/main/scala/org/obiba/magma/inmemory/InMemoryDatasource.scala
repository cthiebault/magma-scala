package org.obiba.magma.inmemory

import java.util.Locale

import org.obiba.magma._

class InMemoryDatasource(override var name: String) extends AbstractDatasource {

  private var _tables: Map[String, ValueTable] = Map()
  private var _attributes: List[Attribute] = List()

  override def `type`: String = "in-memory"

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
    _attributes = List()
  }

  override def canRenameTable(tableName: String): Boolean = hasTable(tableName)

  override def renameTable(tableName: String, newName: String): Unit = {
    val table: ValueTable = getTable(tableName).getOrElse(throw new NoSuchElementException)
    table.name = newName
    _tables = _tables + (newName -> table)
  }

  override def attributes: List[Attribute] = _attributes

  def addAttribute(attribute: Attribute): Unit = {
    val index = indexOf(attribute)
    _attributes = if (index == -1) _attributes :+ attribute else _attributes.updated(index, attribute)
  }

  private def indexOf(attribute: Attribute): Int = {
    _attributes.indexWhere(a => a.name == attribute.name && a.namespace == attribute.namespace && a.locale == attribute.locale)
  }

  override def removeAttributes(name: String): Unit = {
    _attributes = _attributes.filterNot {
      case Attribute(`name`, _, _, _) => true
      case _ => false
    }
  }

  override def removeAttributes(name: String, namespace: String): Unit = {
    _attributes = _attributes.filterNot {
      case Attribute(`name`, Some(`namespace`), _, _) => true
      case _ => false
    }
  }

  override def removeAttribute(name: String, namespace: String, locale: Locale): Unit = {
    _attributes = _attributes.filterNot {
      case Attribute(`name`, Some(`namespace`), Some(`locale`), _) => true
      case _ => false
    }
  }

  override def hasEntities(predicate: ValueTable => Boolean): Boolean = ???

  override def createWriter(tableName: String, entityType: String): ValueTableWriter = ???

}

