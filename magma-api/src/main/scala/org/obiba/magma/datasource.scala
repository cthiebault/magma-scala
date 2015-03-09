package org.obiba.magma

import org.obiba.magma.attribute.AttributeWriter
import org.obiba.magma.entity.EntityType
import org.obiba.magma.time.Timestamped

trait Datasource extends Timestamped with AttributeWriter with Initialisable with Droppable with Disposable {

  val name: String

  def `type`: String

  def tables: Set[ValueTable]

  def getTable(tableName: String): Option[ValueTable]

  def hasTable(tableName: String): Boolean

  def canDropTable(tableName: String): Boolean

  def dropTable(tableName: String)

  def canRenameTable(tableName: String): Boolean

  def renameTable(tableName: String, newName: String)

  def hasEntities(predicate: ValueTable => Boolean): Boolean

  def createWriter(tableName: String, entityType: EntityType): ValueTableWriter

}

abstract class AbstractDatasource extends Datasource {

  private var _tables: Set[ValueTable] = Set()

  override def tables: Set[ValueTable] = _tables

  override def getTable(tableName: String): Option[ValueTable] = tables.find(t => t.name == name)

  override def hasTable(tableName: String): Boolean = getTable(tableName).isDefined

  override def dispose(): Unit = {}

  protected def valueTableNames(): Set[String]

  protected def addTable(table: ValueTable) = _tables = _tables + table

  protected def removeTable(table: ValueTable) = _tables = _tables - table

  protected def initialiseValueTable(tableName: String): ValueTable

  override def initialise(): Unit = {
    valueTableNames()
      .map(initialiseValueTable)
      .foreach(t => {
          Initialisable.initialise(t)
          addTable(t)
        }
      )
  }

}
