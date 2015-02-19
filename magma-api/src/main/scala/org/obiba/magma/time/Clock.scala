package org.obiba.magma.time

import java.time.Instant

trait Clock {

  def now: Long

}

object Clock {

  object InstantNow extends Clock {
    def now: Long = Instant.now.getEpochSecond
  }

}
