package org.obiba.magma.datasource.mongodb

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoClientURI

class MongoDBFactory(val connectionURI: String) {

  lazy val mongoClientURI: MongoClientURI = MongoClientURI(connectionURI)

  lazy val mongoClient: MongoClient = MongoClient(mongoClientURI)

  def close(): Unit = {
    //TODO if mongoClient was never accessed before, it will create mongoClient to close it directly
    mongoClient.close()
  }

  def execute[T](callback: MongoDBCallback[T]): T = {
    callback.doWithDB(mongoClient.getDB(mongoClientURI.database.get))
  }

}

trait MongoDBCallback[T] {
  def doWithDB(db: MongoDB): T
}
