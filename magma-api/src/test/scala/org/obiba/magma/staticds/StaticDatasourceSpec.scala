package org.obiba.magma.staticds

import org.obiba.magma.UnitSpec
import org.obiba.magma.attribute._
import org.obiba.magma.static.StaticDatasource
import org.obiba.magma.value.TextType

class StaticDatasourceSpec extends UnitSpec {

  "An empty static Datasource" should "have no tables" in {
    val ds: StaticDatasource = new StaticDatasource("empty")
    ds.tables should be(empty)
    ds.getTable("none") should be(empty)
  }

  "A static Datasource" should "have default values" in {
    val ds: StaticDatasource = new StaticDatasource("ds")
    ds.`type` should be("static")
    ds.name should be("ds")
  }

  it should "be droppable" in {
    new StaticDatasource("test").canDrop should be(true)
  }

  it should "be empty after drop" in {
    val ds = new StaticDatasource("test")
    ds.addAttribute(Attribute("attr", TextType.valueOf("french value")))
    ds.drop()

    ds.attributes should be(empty)
    ds.tables should be(empty)
  }

//  it should "have table after adding one" in {
//    val ds = new StaticDatasource("test")
//  }

}
