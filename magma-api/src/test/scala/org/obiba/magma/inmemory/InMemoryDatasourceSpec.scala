package org.obiba.magma.inmemory

import java.util.Locale.{ENGLISH, FRENCH}

import org.obiba.magma.UnitSpec
import org.obiba.magma.attribute._
import org.obiba.magma.static.InMemoryDatasource
import org.obiba.magma.value.TextType

class InMemoryDatasourceSpec extends UnitSpec {

  val emptyDs = new InMemoryDatasource("empty")
  "An empty InMemory Datasource" should "have no attributes" in {
    emptyDs.hasAttributes should be(false)
    emptyDs.attributes should be(empty)
  }

  it should "have no tables" in {
    emptyDs.tables should be(empty)
  }

  "A InMemory Datasource" should "have in-memory type" in {
    emptyDs.`type` should be("in-memory")
  }

  it should "be droppable" in {
    new InMemoryDatasource("test").canDrop should be(true)
  }

  it should "be empty after drop" in {
    val ds = new InMemoryDatasource("test")
    ds.addAttribute(Attribute("attr", "namespace", FRENCH, TextType.valueOf("french value")))
    ds.addAttribute(Attribute("attr", "namespace", ENGLISH, TextType.valueOf("english value")))
    ds.drop()

    ds.attributes should be(empty)
    ds.tables should be(empty)
  }

}
