package org.obiba.magma

import org.obiba.magma.value.Value

trait Timestamps {

  def created: Value

  def lastUpdate: Value

}

trait Timestamped {

  def timestamps: Timestamps

}

object NullTimestamps extends Timestamps {
  
  override def created: Value = ???

  override def lastUpdate: Value = ???
}