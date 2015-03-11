package org.obiba.magma.datasource.mongodb

import com.github.simplyscala.{MongoEmbedDatabase, MongodProps}
import de.flapdoodle.embed.mongo.Command
import de.flapdoodle.embed.mongo.config.{ArtifactStoreBuilder, DownloadConfigBuilder, RuntimeConfigBuilder}
import de.flapdoodle.embed.process.config.IRuntimeConfig
import de.flapdoodle.embed.process.config.store.HttpProxyFactory
import org.obiba.magma.UnitSpec
import org.obiba.magma.entity.EntityType
import org.scalatest.BeforeAndAfter

class MongoDBDatasourceSpec extends UnitSpec with MongoEmbedDatabase with BeforeAndAfter {

  private val USE_PROXY = false
  private val MONGODB_URI = "mongodb://localhost:12345/magma-ds"

  private var mongoProps: MongodProps = null

  // configure proxy
  private val runtimeProxyConfig: IRuntimeConfig = new RuntimeConfigBuilder()
    .defaults(Command.MongoD)
    .artifactStore(
      new ArtifactStoreBuilder()
        .defaults(Command.MongoD)
        .download(
          new DownloadConfigBuilder()
            .defaultsForCommand(Command.MongoD)
            .proxyFactory(new HttpProxyFactory("localhost", 3128))
        )
    )
    .build()

  before {
    logger.debug("Start MongoDB")
    mongoProps = if (USE_PROXY) mongoStart(runtimeConfig = runtimeProxyConfig) else mongoStart()
  }

  after {
    logger.debug("Stop MongoDB")
    mongoStop(mongoProps)
  }

  "A MongoDBDatasource" should "have default values" in {
    val ds: MongoDBDatasource = new MongoDBDatasourceFactory("ds1", MONGODB_URI).create()

    ds should not be null
    ds.`type` should be("mongodb")
    ds.name should be("ds1")
    ds.timestamps.created should be(clock.instant)
    ds.timestamps.lastUpdate.get should be(clock.instant)

    ds.canDrop should be(true)
    ds.tables should be(empty)
    ds.getTable("table1") should be(empty)

    ds.mongoDBFactory.db("datasource").size should be(1)
  }

  it should "have a table after adding one" in {
    val ds: MongoDBDatasource = new MongoDBDatasourceFactory("ds1", MONGODB_URI).create()
    ds.createWriter("table", EntityType.Participant)

    ds.tables should have size 1
    ds.hasTable("table") should be(true)

    val table = ds.getTable("table")
    table should be('defined)
    table.get.name should be("table")

    ds.createWriter("table", EntityType.Participant)
    ds.tables should have size 1

    ds.mongoDBFactory.db("datasource").size should be(1)
    ds.mongoDBFactory.db("value_table").size should be(1)

    ds.mongoDBFactory.db("datasource").find().foreach(logger.debug("datasource: {}", _))
    ds.mongoDBFactory.db("value_table").find().foreach(logger.debug("table: {}", _))
  }

}
