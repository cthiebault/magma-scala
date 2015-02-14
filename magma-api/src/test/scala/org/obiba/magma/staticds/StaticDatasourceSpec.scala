package org.obiba.magma.staticds

import org.obiba.magma.UnitSpec
import org.obiba.magma.attribute._
import org.obiba.magma.static.StaticDatasource
import org.obiba.magma.value.TextType

class StaticDatasourceSpec extends UnitSpec {

  "An empty static Datasource" should "have no tables" in {
    new StaticDatasource("empty").tables should be(empty)
  }

  "A static Datasource" should "have static type" in {
    new StaticDatasource("empty").`type` should be("static")
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

}