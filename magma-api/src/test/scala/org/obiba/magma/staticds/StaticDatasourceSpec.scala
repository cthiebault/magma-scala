package org.obiba.magma.staticds

import org.obiba.magma.UnitSpec
import org.obiba.magma.attribute._
import org.obiba.magma.entity.EntityType
import org.obiba.magma.value.JavaObjToValueConverters.StringConverters

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
    ds.addAttribute(Attribute("attr", "french value".toTextValue))
    ds.drop()

    ds.attributes should be(empty)
    ds.tables should be(empty)
  }

  it should "have a table after adding one" in {
    val ds = new StaticDatasource("test")
    ds.createWriter("table", EntityType.Participant)

    ds.tables should have size 1
    ds.hasTable("table") should be(true)

    val table = ds.getTable("table")
    table should be('defined)
    table.get.name should be("table")

    ds.createWriter("table", EntityType.Participant)
    ds.tables should have size 1
  }

  it can "rename table" in {
    val ds = new StaticDatasource("test")
    ds.createWriter("table", EntityType.Participant)
    ds.renameTable("table", "new_table")

    ds.tables should have size 1
    ds.hasTable("table") should be(false)
    ds.hasTable("new_table") should be(true)

    val table = ds.getTable("new_table")
    table should be('defined)
    table.get.name should be("new_table")
  }

  it can "drop table" in {
    val ds = new StaticDatasource("test")
    ds.createWriter("table", EntityType.Participant)
    ds.canDropTable("table") should be(true)

    ds.dropTable("table")
    ds.tables should be(empty)
  }

}
