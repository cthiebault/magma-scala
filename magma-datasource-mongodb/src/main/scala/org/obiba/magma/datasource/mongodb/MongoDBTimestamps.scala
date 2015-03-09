package org.obiba.magma.datasource.mongodb

import java.time.{Clock, Instant}

import com.mongodb.casbah.commons.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import org.obiba.magma.datasource.mongodb.MongoDBTimestamps.{CREATED_FIELD, TIMESTAMPS_FIELD, UPDATE_FIELD}
import org.obiba.magma.logging.Slf4jLogging
import org.obiba.magma.time.Timestamps

class MongoDBTimestamps(dBObject: DBObject)(implicit clock: Clock) extends Timestamps with Slf4jLogging {

  private def timestampsObject(): DBObject = dBObject.getAs[DBObject](TIMESTAMPS_FIELD).get

  override def created: Instant = {
    Instant.parse(timestampsObject().get(CREATED_FIELD).toString)
  }

  override def lastUpdate: Option[Instant] = {
    val str = timestampsObject().get(UPDATE_FIELD).toString
    if (str == null) None else Some(Instant.parse(str))
  }

  def update(instant: Instant = Instant.now(clock)): Unit = {
    timestampsObject += UPDATE_FIELD -> instant
  }

}

object MongoDBTimestamps {

  val TIMESTAMPS_FIELD = "_timestamps"
  val CREATED_FIELD = "created"
  val UPDATE_FIELD = "update"

  protected[mongodb] def createTimestampsObject()(implicit clock: Clock): DBObject = {
    val now: Instant = Instant.now(clock)
    MongoDBObject(CREATED_FIELD -> now, UPDATE_FIELD -> now)
  }

}

