package org.obiba.magma.datasource.staticds

import org.obiba.magma.{Datasource, UnitSpec}

class StaticDatasourceFactorySpec extends UnitSpec {

  "A StaticDatasourceFactory" should "create StaticDatasource" in {
    val ds: Datasource = new StaticDatasourceFactory("ds1").create
    ds should be(a[StaticDatasource])
    ds.name should be("ds1")
  }

}
