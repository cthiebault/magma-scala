package org.obiba.magma.datasource.mongodb

import java.time.{Clock, Instant, ZoneId}

import com.github.simplyscala.{MongoEmbedDatabase, MongodProps}
import org.obiba.magma.UnitSpec
import org.scalatest.BeforeAndAfter

class MongoDBDatasourceSpec extends UnitSpec with MongoEmbedDatabase with BeforeAndAfter {

  private var mongoProps: MongodProps = null

  before {
    logger.debug("Start MongoDB")
    mongoProps = mongoStart() // by default port = 12345
  }

  after {
    logger.debug("Stop MongoDB")
    mongoStop(mongoProps)
  }

  "A MongoDBDatasource" should "be writable in MongoDB" in {

    implicit val clock: Clock = Clock.fixed(Instant.now, ZoneId.systemDefault)

    val ds: MongoDBDatasource = new MongoDBDatasourceFactory("ds1", "mongodb://localhost:12345/magma-ds").create()

    ds should not be null
    ds.`type` should be("mongodb")
    ds.name should be("ds1")
    ds.timestamps.created should be(clock.instant)
    ds.timestamps.lastUpdate.get should be(clock.instant)

    ds.mongoDBFactory.db("datasource").size should be(1)

  }

}
