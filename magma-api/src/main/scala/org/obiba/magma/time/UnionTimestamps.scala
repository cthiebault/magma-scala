package org.obiba.magma.time

import java.time.Instant

class UnionTimestamps(val timestamps: Timestamps*) extends Timestamps {

  override def created: Instant = {
    timestamps
      .map(_.created)
      .sorted
      .head
  }

  override def lastUpdate: Option[Instant] = {
    timestamps
      .map(_.lastUpdate)
      .filterNot(_.isEmpty)
      .map(_.get)
      .sorted(Ordering[Instant].reverse)
      .headOption
  }

}

object UnionTimestamps {

  def apply(timestamped: Timestamped*): UnionTimestamps = {
    new UnionTimestamps(timestamped.map(_.timestamps): _*) // :_* placeholder accepting any value + vararg operator
  }

  def apply(timestamped: Traversable[Timestamped]): UnionTimestamps = {
    new UnionTimestamps(timestamped.map(_.timestamps).toList: _*) // :_* placeholder accepting any value + vararg operator
  }

}


