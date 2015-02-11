package org.obiba.magma

trait Datasource extends AttributeWriter {

  def getName: String

  def getType: String

  def hasValueTable(tableName: String): Boolean

  def hasEntities(predicate: Nothing): Boolean

  def getValueTable(tableName: String): Option[ValueTable]

  def getValueTables: Set[ValueTable]

  def canDropTable(tableName: String): Boolean

  def dropTable(tableName: String)

  def canRenameTable(tableName: String): Boolean

  def renameTable(tableName: String, newName: String)

  def drop(): Unit

  def canDrop: Boolean

  def createWriter(tableName: String, entityType: String): ValueTableWriter

}

abstract class AbstractDatasource extends Datasource {

}