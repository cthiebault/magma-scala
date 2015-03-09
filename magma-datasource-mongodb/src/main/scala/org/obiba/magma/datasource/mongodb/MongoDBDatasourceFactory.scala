package org.obiba.magma.datasource.mongodb

import java.io.ByteArrayInputStream
import java.net.URI
import java.time.Clock
import java.util.Properties

import com.google.common.base.Charsets
import org.apache.http.client.utils.URIBuilder
import org.obiba.magma.utils.StringUtils.StringsWrapper
import org.obiba.magma.{Datasource, DatasourceFactory}

import scala.collection.JavaConversions._

/**
 *
 * @param name
 * @param url mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]
 * @param clock
 */
class MongoDBDatasourceFactory(
  var name: String,
  val url: String,
  val username: String = null,
  val password: String = null,
  val options: String = null)
  (implicit clock: Clock)
  extends DatasourceFactory {

  override def create(): MongoDBDatasource = new MongoDBDatasource(name, new MongoDBFactory(buildUri.toString))

  private def buildUri: URI = {
    val uriBuilder: URIBuilder = new URIBuilder(url)
    if (!username.isNullOrEmpty) {
      if (password.isNullOrEmpty) {
        uriBuilder.setUserInfo(username)
      }
      else {
        uriBuilder.setUserInfo(username, password)
      }
    }
    val prop: Properties = readOptions
    for (entry <- prop.entrySet) {
      uriBuilder.addParameter(entry.getKey.toString, entry.getValue.toString)
    }
    uriBuilder.build
  }

  private def readOptions: Properties = {
    val prop: Properties = new Properties
    if (!options.isNullOrEmpty) {
      prop.load(new ByteArrayInputStream(options.getBytes(Charsets.UTF_8)))
    }
    prop
  }

}

