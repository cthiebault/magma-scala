package org.obiba.magma.inmemory

import org.obiba.magma._

class InMemoryValueTable(override var name: String) extends AbstractValueTable {

  override def canDrop: Boolean = true

  override def drop(): Unit = ???
}
