package org.obiba.magma.value

import org.obiba.magma.UnitSpec
import org.obiba.magma.value.JavaObjToValueConverters.StringConverters

class TextTypeSpec extends UnitSpec {

  "A TextType" should "be named" in {
    TextType.name should be("text")
  }

  it should "have value for non null param" in {
    TextType.valueOf("foo").get.value.get should be("foo")
  }

  it should "have no value for null param" in {
    TextType.valueOf(null).get.value should be(None)
  }

  it should "be equals to toString()" in {
    TextType.valueOf(1).get.value.get should be("1")
  }

  it should "be equals to same TextType value build without String param" in {
    TextType.valueOf(1) should be(TextType.valueOf("1"))
  }

  it should "have toString equals to value" in {
    TextType.toString(TextType.valueOf("foo").get) should be("foo")
  }

  it should "have toString equals to null for null value" in {
    TextType.toString(TextType.nullValue) should be(null)
  }

  it should "sort values" in {
    TextType.compare("d".toTextValue, "a".toTextValue) should be > 0
    TextType.compare("d".toTextValue, TextType.nullValue) should be > 0
    TextType.compare("a".toTextValue, "d".toTextValue) should be < 0
    TextType.compare(TextType.nullValue, "d".toTextValue) should be < 0
    TextType.compare("a".toTextValue, "a".toTextValue) should be(0)
    TextType.compare(TextType.nullValue, TextType.nullValue) should be(0)
  }
}
