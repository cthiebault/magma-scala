package org.obiba.magma

trait Droppable {

  def canDrop: Boolean

  def drop(): Unit

}

class NonDroppable extends Droppable {

  override def canDrop: Boolean = false

  override def drop(): Unit = throw new UnsupportedOperationException("cannot drop")
}
