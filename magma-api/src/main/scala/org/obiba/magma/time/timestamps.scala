package org.obiba.magma.time

import java.time.Instant

trait Timestamps {

  def created: Instant

  def lastUpdate: Instant

}

case class TimestampsBean(created: Instant = Instant.now(), private var _lastUpdate: Instant = null) extends Timestamps {

  override def lastUpdate: Instant = _lastUpdate

  def touch(): Unit = _lastUpdate = Instant.now()
}

trait Timestamped {

  def timestamps: Timestamps

}
