package org.obiba.magma

trait Disposable {

  def dispose(); Unit

}

object Disposable {

  def dispose(disposes: Any*): Unit = {
    disposes
      .filter(_.isInstanceOf[Disposable])
      .map(_.asInstanceOf[Disposable])
      .foreach(_.dispose())
  }

}
