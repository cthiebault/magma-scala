package org.obiba.magma

import org.obiba.magma.attribute.AttributeWriter
import org.obiba.magma.entity.EntityType
import org.obiba.magma.time.Timestamped

import scala.collection._

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

  private val _tables: mutable.LinkedHashSet[ValueTable] = mutable.LinkedHashSet()

  override def tables: Set[ValueTable] = _tables.toSet

  override def getTable(tableName: String): Option[ValueTable] = _tables.find(_.name == tableName)

  override def hasTable(tableName: String): Boolean = getTable(tableName).isDefined

  override def dispose(): Unit = Disposable.dispose(tables)

  protected def valueTableNames(): Set[String]

  protected def addTable(table: ValueTable) = _tables.add(table)

  protected def removeTable(table: ValueTable) = _tables.remove(table)

  protected def initialiseValueTable(tableName: String): ValueTable

  override def initialise(): Unit = {
    valueTableNames()
      .map(initialiseValueTable)
      .foreach(initAndAddTable)
  }

  private def initAndAddTable(table: ValueTable): Unit = {
    Initialisable.initialise(table)
    addTable(table)
  }

}
