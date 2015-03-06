package org.obiba.magma.time

import java.time.Instant

class UnionTimestamps(val timestamps: Traversable[Timestamps]) extends Timestamps {

  override def created: Instant = ???

  override def lastUpdate: Instant = ???

}

object UnionTimestamps {

  def apply(timestamped: Traversable[Timestamped]): UnionTimestamps = {
    new UnionTimestamps(timestamped.map(_.timestamps))
  }

}


