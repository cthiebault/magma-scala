package org.obiba.magma

trait Droppable {

  def canDrop: Boolean

  def drop(): Unit

}


