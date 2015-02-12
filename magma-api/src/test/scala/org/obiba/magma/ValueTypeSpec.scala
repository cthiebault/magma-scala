package org.obiba.magma

import org.scalatest._

class ValueTypeSpec extends FlatSpec with Matchers {

  "A TextType" should "be named" in {
    TextType.name should be("text")
  }
  it should "have value for non null param" in {
    TextType.valueOf("foo").value should be(Some("foo"))
  }
  it should "have no value for null param" in {
    TextType.valueOf(null).value should be(None)
  }
  it should "be equals to toString()" in {
    TextType.valueOf(1).value.get should be("1")
  }
  it should "be equals to same TextType value build without String param" in {
    TextType.valueOf(1) should be(TextType.valueOf("1"))
  }
  it should "have toString equals to value" in {
    TextType.toString(TextType.valueOf("foo")) should be("foo")
  }
  it should "have toString equals to null for null value" in {
    TextType.toString(TextType.nullValue) should be(null)
  }

}
