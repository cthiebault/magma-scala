package org.obiba.magma.time

import java.time.Instant

import org.obiba.magma.UnitSpec

class UnionTimestampsSpec extends UnitSpec {

  "A UnionTimestamps" should "return first created date" in {
    val t1 = TimestampsBean()
    val t2 = TimestampsBean(Instant.now.minusSeconds(10))
    val t3 = TimestampsBean(Instant.now.plusSeconds(10))
    new UnionTimestamps(t1, t2, t3).created should be(t2.created)
  }

  it should "return last lastUpdate date" in {
    val t1 = TimestampsBean(Instant.now, Instant.now)
    val t2 = TimestampsBean(Instant.now, Instant.now.minusSeconds(10))
    val t3 = TimestampsBean(Instant.now, Instant.now.plusSeconds(10))
    val t4 = TimestampsBean()
    new UnionTimestamps(t1, t2, t3).lastUpdate.get should be(t3.lastUpdate.get)
  }

  it should "return empty lastUpdate for all empty lastUpdate" in {
    new UnionTimestamps(TimestampsBean(), TimestampsBean(), TimestampsBean()).lastUpdate should be('empty)
  }

  class TimestampedStub(val _instant: Instant) extends Timestamped {
    override def timestamps: Timestamps = TimestampsBean(_instant, _instant.plusSeconds(10))
  }

  it can "be created from Timestampeds" in {
    val t1 = new TimestampedStub(Instant.now)
    val t2 = new TimestampedStub(Instant.now.minusSeconds(10))
    val t3 = new TimestampedStub(Instant.now.plusSeconds(10))
    val varargUnionTimestamps: UnionTimestamps = UnionTimestamps(t1, t2, t3)
    val listUnionTimestamps: UnionTimestamps = UnionTimestamps(List(t1, t2, t3))
    varargUnionTimestamps.created should be(t2.timestamps.created)
    listUnionTimestamps.created should be(t2.timestamps.created)
    varargUnionTimestamps.lastUpdate.get should be(t3.timestamps.lastUpdate.get)
    listUnionTimestamps.lastUpdate.get should be(t3.timestamps.lastUpdate.get)
  }
}
