package org.obiba.magma

import org.obiba.magma.Datasource

trait ValueTable extends Droppable {

  var name: String

  def datasource: Datasource

  def entityType: String

  def entities: Set[Entity]

  def getEntityCount: Int

}

abstract class AbstractValueTable extends ValueTable {

}
