package org.obiba.magma.attribute

import java.util.Locale.{ENGLISH, FRENCH, GERMAN}

import org.obiba.magma.UnitSpec
import org.obiba.magma.value.JavaObjToValueConverters.StringConverters

class AttributeSpec extends UnitSpec {

  class TestAttributeWriter extends ListAttributeWriter

  "An empty AttributeAware" should "have no attributes" in {
    new TestAttributeWriter().hasAttributes should be(false)
    new TestAttributeWriter().attributes should be(empty)
  }

  "An AttributeAware" should "have attributes after writing" in {
    val writer = new TestAttributeWriter()
    writer.addAttribute(Attribute("attr", "namespace", FRENCH, "french value".toTextValue))
    writer.attributes should have size 1

    writer.addAttribute(Attribute("attr", "namespace", ENGLISH, "english value".toTextValue))
    writer.attributes should have size 2

    writer.getAttributeValue("attr", "namespace", FRENCH).get should be("french value".toTextValue)
    writer.attributes should be(List(
      Attribute("attr", "namespace", FRENCH, "french value".toTextValue),
      Attribute("attr", "namespace", ENGLISH, "english value".toTextValue)))
  }

  it should "have new attribute after replacement" in {
    val writer = new TestAttributeWriter()
    writer.addAttribute(Attribute("attr", "namespace", FRENCH, "french value".toTextValue))
    writer.addAttribute(Attribute("attr", "namespace", ENGLISH, "english value".toTextValue))
    writer.addAttribute(Attribute("attr", "namespace", FRENCH, "new french value".toTextValue))
    writer.attributes should have size 2
    writer.getAttributeValue("attr", "namespace", FRENCH).get should be("new french value".toTextValue)
  }

  it should "have no attributes after removing" in {
    val writer = new TestAttributeWriter()
    writer.addAttribute(Attribute("attr", "namespace", FRENCH, "french value".toTextValue))
    writer.removeAttribute("attr", "namespace", FRENCH)
    writer.attributes should be(empty)
  }

  it should "have one attribute after removing by locale" in {
    val writer = new TestAttributeWriter()
    writer.addAttribute(Attribute("attr", "namespace", FRENCH, "french value".toTextValue))
    writer.addAttribute(Attribute("attr", "namespace", ENGLISH, "english value".toTextValue))
    writer.attributes should have size 2

    writer.removeAttribute("attr", "namespace", GERMAN)
    writer.attributes should have size 2

    writer.removeAttribute("attr", "namespace", FRENCH)
    writer.attributes should have size 1
  }

  it should "have no attributes after removing by namespace" in {
    val writer = new TestAttributeWriter()
    writer.addAttribute(Attribute("attr", "namespace", FRENCH, "french value".toTextValue))
    writer.addAttribute(Attribute("attr", "namespace", ENGLISH, "english value".toTextValue))
    writer.attributes should have size 2

    writer.removeAttributes("attr", "other namespace")
    writer.attributes should have size 2

    writer.removeAttributes("attr", "namespace")
    writer.attributes should be(empty)
  }

  it should "have no attributes after removing by name" in {
    val writer = new TestAttributeWriter()
    writer.addAttribute(Attribute("attr", "namespace", FRENCH, "french value".toTextValue))
    writer.addAttribute(Attribute("attr", "namespace", ENGLISH, "english value".toTextValue))
    writer.attributes should have size 2

    writer.removeAttributes("other attr")
    writer.attributes should have size 2

    writer.removeAttributes("attr")
    writer.attributes should be(empty)
  }

  val attrWriter = new TestAttributeWriter()
  attrWriter.addAttribute(Attribute("attr1", "namespace", FRENCH, "french value 1".toTextValue))
  attrWriter.addAttribute(Attribute("attr1", "namespace", ENGLISH, "english value 1".toTextValue))
  attrWriter.addAttribute(Attribute("attr2", "namespace", "value 2".toTextValue))
  attrWriter.addAttribute(Attribute("attr3", "value 3".toTextValue))

  it should "have 3 attributes [namespace]" in {
    attrWriter.getAttributes(namespace = "namespace") should have size 3
  }
  it should "have 2 attributes [namespace.attr1]" in {
    attrWriter.getAttributes("attr1", "namespace") should have size 2
  }
  it should "have an attribute [namespace.attr1.FRENCH]" in {
    attrWriter.getAttribute("attr1", "namespace", FRENCH).get should
        be(Attribute("attr1", "namespace", FRENCH, "french value 1".toTextValue))
  }
  it should "have an attribute value [namespace.attr1.FRENCH]" in {
    attrWriter.getAttributeValue("attr1", "namespace", FRENCH).get should be("french value 1".toTextValue)
  }
  it should "not have an attribute [namespace.attr1.GERMAN]" in {
    attrWriter.getAttribute("attr1", "namespace", GERMAN) should be(None)
  }
  it should "not have an attribute value [namespace.attr1.GERMAN]" in {
    attrWriter.getAttributeValue("attr1", "namespace", GERMAN) should be(None)
  }

}
