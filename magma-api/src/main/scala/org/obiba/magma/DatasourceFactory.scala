package org.obiba.magma

trait DatasourceFactory {

  var name: String

  def create(): Datasource

}
