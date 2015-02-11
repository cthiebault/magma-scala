package org.obiba.magma

import java.util.Locale
import java.util.Locale.{FRENCH, ENGLISH, GERMAN}


class AttributeSpec extends UnitSpec {

  private case class TestAttributeAware(attributes: List[Attribute] = Nil) extends AttributeAware

  "An empty AttributeAware" should "have no attributes" in {
    new TestAttributeAware().hasAttributes should be(false)
    new TestAttributeAware().attributes should be(empty)
  }

  private val attributeAware: TestAttributeAware = new TestAttributeAware(
    List(
      Attribute("attr1", "namespace", FRENCH, TextType.valueOf("french value 1")),
      Attribute("attr1", "namespace", ENGLISH, TextType.valueOf("english value 1")),
      Attribute("attr2", "namespace", TextType.valueOf("value 2")),
      Attribute("attr3", TextType.valueOf("value 3"))
    )
  )

  "A non empty AttributeAware" should "have attributes" in {
    attributeAware.hasAttributes should be(true)
    attributeAware.attributes should not be empty
    attributeAware.attributes should have size 4
  }
  it should "have 3 attributes [namespace]" in {
    attributeAware.getAttributes(namespace = "namespace") should have size 3
  }
  it should "have 2 attributes [namespace.attr1]" in {
    attributeAware.getAttributes("attr1", "namespace") should have size 2
  }
  it should "have an attribute [namespace.attr1.FRENCH]" in {
    attributeAware.getAttribute("attr1", "namespace", FRENCH).get should be(Attribute("attr1", "namespace", FRENCH, TextType.valueOf("french value 1")))
  }
  it should "have an attribute value [namespace.attr1.FRENCH]" in {
    attributeAware.getAttributeValue("attr1", "namespace", FRENCH).get should be(TextType.valueOf("french value 1"))
  }
  it should "not have an attribute [namespace.attr1.GERMAN]" in {
    attributeAware.getAttribute("attr1", "namespace", GERMAN) should be(None)
  }
  it should "not have an attribute value [namespace.attr1.GERMAN]" in {
    attributeAware.getAttributeValue("attr1", "namespace", GERMAN) should be(None)
  }

  private class TestAttributeWriter(private var _attributes: List[Attribute] = Nil) extends AttributeWriter {

    override def attributes: List[Attribute] = _attributes

    override def attributeValue(name: String, namespace: String, locale: Locale, value: Value): Unit = {
      _attributes = _attributes ::: List(Attribute(name, namespace, locale, value))
    }
  }

  "An AttributeWriter" should "have attributes after writing" in {
    val attributeWriter: TestAttributeWriter = new TestAttributeWriter()
    attributeWriter.attributeValue("attr", "namespace", FRENCH, TextType.valueOf("french value 1"))

    attributeWriter.attributes should have size 1
    attributeWriter.getAttributeValue("attr", "namespace", FRENCH).get should be(TextType.valueOf("french value 1"))
  }
}
