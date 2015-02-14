package org.obiba.magma.inmemory

import java.util.Locale.{ENGLISH, FRENCH, GERMAN}

import org.obiba.magma.UnitSpec
import org.obiba.magma.attribute._
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

  it should "have attributes after writing" in {
    val ds = new InMemoryDatasource("test")
    ds.addAttribute(Attribute("attr", "namespace", FRENCH, TextType.valueOf("french value")))
    ds.attributes should have size 1
    ds.addAttribute(Attribute("attr", "namespace", ENGLISH, TextType.valueOf("english value")))
    ds.attributes should have size 2
    ds.getAttributeValue("attr", "namespace", FRENCH).get should be(TextType.valueOf("french value"))
    ds.attributes should be(List(
      Attribute("attr", "namespace", FRENCH, TextType.valueOf("french value")),
      Attribute("attr", "namespace", ENGLISH, TextType.valueOf("english value"))))
  }

  it should "have new attribute after replacement" in {
    val ds = new InMemoryDatasource("test")
    ds.addAttribute(Attribute("attr", "namespace", FRENCH, TextType.valueOf("french value")))
    ds.addAttribute(Attribute("attr", "namespace", ENGLISH, TextType.valueOf("english value")))
    ds.addAttribute(Attribute("attr", "namespace", FRENCH, TextType.valueOf("new french value")))
    ds.attributes should have size 2
    ds.getAttributeValue("attr", "namespace", FRENCH).get should be(TextType.valueOf("new french value"))
  }

  it should "have no attributes after removing" in {
    val ds = new InMemoryDatasource("test")
    ds.addAttribute(Attribute("attr", "namespace", FRENCH, TextType.valueOf("french value")))
    ds.removeAttribute("attr", "namespace", FRENCH)
    ds.attributes should be(empty)
  }

  it should "have one attribute after removing by locale" in {
    val ds = new InMemoryDatasource("test")
    ds.addAttribute(Attribute("attr", "namespace", FRENCH, TextType.valueOf("french value")))
    ds.addAttribute(Attribute("attr", "namespace", ENGLISH, TextType.valueOf("english value")))
    ds.attributes should have size 2

    ds.removeAttribute("attr", "namespace", GERMAN)
    ds.attributes should have size 2

    ds.removeAttribute("attr", "namespace", FRENCH)
    ds.attributes should have size 1
  }

  it should "have no attributes after removing by namespace" in {
    val ds = new InMemoryDatasource("test")
    ds.addAttribute(Attribute("attr", "namespace", FRENCH, TextType.valueOf("french value")))
    ds.addAttribute(Attribute("attr", "namespace", ENGLISH, TextType.valueOf("english value")))
    ds.attributes should have size 2

    ds.removeAttributes("attr", "other namespace")
    ds.attributes should have size 2

    ds.removeAttributes("attr", "namespace")
    ds.attributes should be(empty)
  }

  it should "have no attributes after removing by name" in {
    val ds = new InMemoryDatasource("test")
    ds.addAttribute(Attribute("attr", "namespace", FRENCH, TextType.valueOf("french value")))
    ds.addAttribute(Attribute("attr", "namespace", ENGLISH, TextType.valueOf("english value")))
    ds.attributes should have size 2

    ds.removeAttributes("other attr")
    ds.attributes should have size 2

    ds.removeAttributes("attr")
    ds.attributes should be(empty)
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
