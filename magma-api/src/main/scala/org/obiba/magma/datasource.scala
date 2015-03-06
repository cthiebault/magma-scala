package org.obiba.magma

import org.obiba.magma.attribute.AttributeWriter
import org.obiba.magma.entity.EntityType
import org.obiba.magma.time.Timestamped

trait Datasource extends Timestamped with AttributeWriter with Initialisable with Droppable with Disposable {

  var name: String

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

  override def getTable(tableName: String): Option[ValueTable] = tables.find(t => t.name == name)

  override def hasTable(tableName: String): Boolean = getTable(tableName).isDefined

  override def dispose(): Unit = {}
}
