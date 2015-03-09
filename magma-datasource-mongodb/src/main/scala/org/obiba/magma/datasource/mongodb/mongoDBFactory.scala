package org.obiba.magma.datasource.mongodb

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoClientURI
import org.obiba.magma.datasource.mongodb.support.JavaDateTimeConversionHelpers

class MongoDBFactory(val connectionURI: String) {

  // Ensure that the core Serialization helpers are registered.
  EnsureConversionHelpersRegistration.ensure

  lazy val mongoClientURI: MongoClientURI = MongoClientURI(connectionURI)

  lazy val mongoClient: MongoClient = MongoClient(mongoClientURI)

  def close(): Unit = {
    //TODO if mongoClient was never accessed before, it will create mongoClient to close it directly
    mongoClient.close()
  }

  def execute[T](callback: MongoDBCallback[T]): T = {
    callback.doWithDB(db)
  }

  private[mongodb] def db: MongoDB = mongoClient.getDB(mongoClientURI.database.get)

}

trait MongoDBCallback[T] {
  def doWithDB(db: MongoDB): T
}

object EnsureConversionHelpersRegistration {
  lazy val ensure = JavaDateTimeConversionHelpers()
}