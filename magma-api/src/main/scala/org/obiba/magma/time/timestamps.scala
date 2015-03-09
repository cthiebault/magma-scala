package org.obiba.magma.time

import java.time.{Clock, Instant}

trait Timestamps {

  def created: Instant

  def lastUpdate: Option[Instant]

}

case class TimestampsBean(created: Instant = Instant.now(), private var _lastUpdate: Instant = null)(implicit clock: Clock)
  extends Timestamps {

  override def lastUpdate: Option[Instant] = if (_lastUpdate == null) None else Some(_lastUpdate)

  def touch(): Unit = _lastUpdate = clock.instant()
}

trait Timestamped {

  def timestamps: Timestamps

}
