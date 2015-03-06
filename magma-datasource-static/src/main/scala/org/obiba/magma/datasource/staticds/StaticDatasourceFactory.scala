package org.obiba.magma.datasource.staticds

import java.time.Clock

import org.obiba.magma.{Datasource, DatasourceFactory}

class StaticDatasourceFactory(var name: String)(implicit clock: Clock) extends DatasourceFactory {

  override def create: Datasource = new StaticDatasource(name)

}
