package org.obiba.magma

trait Initialisable {

  def initialise(): Unit

}

object Initialisable {

  def initialise(initialisables: Any*): Unit = {
    initialisables
        .filter(_.isInstanceOf[Initialisable])
        .map(_.asInstanceOf[Initialisable])
        .foreach(_.initialise())
  }

}