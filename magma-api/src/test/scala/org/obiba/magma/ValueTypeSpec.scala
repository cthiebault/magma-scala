package org.obiba.magma

import org.scalatest._

class ValueTypeSpec extends FlatSpec with Matchers {

  "A TextType value" should "be equals to same TextType value" in {
    TextType.valueOf("test") should be(TextType.valueOf("test"))
  }
  it should "have text name" in {
    TextType.getName should be("text")
  }
  it should "have value for non null param" in {
    TextType.valueOf("foo").getValue should be(Some("foo"))
  }
  it should "have no value for null param" in {
    TextType.valueOf(null).getValue should be(None)
  }
  it should "be equals to toString()" in {
    TextType.valueOf(1).getValue should be(Some("1"))
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