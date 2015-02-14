package org.obiba.magma.attribute

import java.util.Locale.{ENGLISH, FRENCH, GERMAN}

import org.obiba.magma.UnitSpec
import org.obiba.magma.value._

class AttributeSpec extends UnitSpec {

  class TestAttributeWriter extends ListAttributeWriter

  "An empty AttributeAware" should "have no attributes" in {
    new TestAttributeWriter().hasAttributes should be(false)
    new TestAttributeWriter().attributes should be(empty)
  }

  "An AttributeAware" should "have attributes after writing" in {
    val a = new TestAttributeWriter()
    a.addAttribute(Attribute("attr", "namespace", FRENCH, TextType.valueOf("french value")))
    a.attributes should have size 1

    a.addAttribute(Attribute("attr", "namespace", ENGLISH, TextType.valueOf("english value")))
    a.attributes should have size 2

    a.getAttributeValue("attr", "namespace", FRENCH).get should be(TextType.valueOf("french value"))
    a.attributes should be(List(
      Attribute("attr", "namespace", FRENCH, TextType.valueOf("french value")),
      Attribute("attr", "namespace", ENGLISH, TextType.valueOf("english value"))))
  }

  it should "have new attribute after replacement" in {
    val a = new TestAttributeWriter()
    a.addAttribute(Attribute("attr", "namespace", FRENCH, TextType.valueOf("french value")))
    a.addAttribute(Attribute("attr", "namespace", ENGLISH, TextType.valueOf("english value")))
    a.addAttribute(Attribute("attr", "namespace", FRENCH, TextType.valueOf("new french value")))
    a.attributes should have size 2
    a.getAttributeValue("attr", "namespace", FRENCH).get should be(TextType.valueOf("new french value"))
  }

  it should "have no attributes after removing" in {
    val a = new TestAttributeWriter()
    a.addAttribute(Attribute("attr", "namespace", FRENCH, TextType.valueOf("french value")))
    a.removeAttribute("attr", "namespace", FRENCH)
    a.attributes should be(empty)
  }

  it should "have one attribute after removing by locale" in {
    val a = new TestAttributeWriter()
    a.addAttribute(Attribute("attr", "namespace", FRENCH, TextType.valueOf("french value")))
    a.addAttribute(Attribute("attr", "namespace", ENGLISH, TextType.valueOf("english value")))
    a.attributes should have size 2

    a.removeAttribute("attr", "namespace", GERMAN)
    a.attributes should have size 2

    a.removeAttribute("attr", "namespace", FRENCH)
    a.attributes should have size 1
  }

  it should "have no attributes after removing by namespace" in {
    val a = new TestAttributeWriter()
    a.addAttribute(Attribute("attr", "namespace", FRENCH, TextType.valueOf("french value")))
    a.addAttribute(Attribute("attr", "namespace", ENGLISH, TextType.valueOf("english value")))
    a.attributes should have size 2

    a.removeAttributes("attr", "other namespace")
    a.attributes should have size 2

    a.removeAttributes("attr", "namespace")
    a.attributes should be(empty)
  }

  it should "have no attributes after removing by name" in {
    val a = new TestAttributeWriter()
    a.addAttribute(Attribute("attr", "namespace", FRENCH, TextType.valueOf("french value")))
    a.addAttribute(Attribute("attr", "namespace", ENGLISH, TextType.valueOf("english value")))
    a.attributes should have size 2

    a.removeAttributes("other attr")
    a.attributes should have size 2

    a.removeAttributes("attr")
    a.attributes should be(empty)
  }

  val test = new TestAttributeWriter()
  test.addAttribute(Attribute("attr1", "namespace", FRENCH, TextType.valueOf("french value 1")))
  test.addAttribute(Attribute("attr1", "namespace", ENGLISH, TextType.valueOf("english value 1")))
  test.addAttribute(Attribute("attr2", "namespace", TextType.valueOf("value 2")))
  test.addAttribute(Attribute("attr3", TextType.valueOf("value 3")))

  it should "have 3 attributes [namespace]" in {
    test.getAttributes(namespace = "namespace") should have size 3
  }
  it should "have 2 attributes [namespace.attr1]" in {
    test.getAttributes("attr1", "namespace") should have size 2
  }
  it should "have an attribute [namespace.attr1.FRENCH]" in {
    test.getAttribute("attr1", "namespace", FRENCH).get should
        be(Attribute("attr1", "namespace", FRENCH, TextType.valueOf("french value 1")))
  }
  it should "have an attribute value [namespace.attr1.FRENCH]" in {
    test.getAttributeValue("attr1", "namespace", FRENCH).get should be(TextType.valueOf("french value 1"))
  }
  it should "not have an attribute [namespace.attr1.GERMAN]" in {
    test.getAttribute("attr1", "namespace", GERMAN) should be(None)
  }
  it should "not have an attribute value [namespace.attr1.GERMAN]" in {
    test.getAttributeValue("attr1", "namespace", GERMAN) should be(None)
  }


}
