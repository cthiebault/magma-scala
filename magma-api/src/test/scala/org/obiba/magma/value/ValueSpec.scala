package org.obiba.magma.value

import org.obiba.magma.UnitSpec
import org.obiba.magma.value.ValueConverters.StringConverters

class ValueSpec extends UnitSpec {

  "A Value" should "be equals to same value" in {
    TextType.valueOf("test") should be(TextType.valueOf("test"))
    "test".toTextValue should be(TextType.valueOf("test"))
  }

  it should "have the right length for non null value" in {
    TextType.valueOf("test").length should be(4)
  }

  it should "have 0 length for null value" in {
    TextType.nullValue.length should be(0)
  }

  it should "be sortable" in {
    val list: List[Value] = List("d", "c", "b", "a").map(_.toTextValue)
    list
      .sorted(ValueComparator)
      .map(_.toString)
      .mkString(",") should be("a,b,c,d")
  }

  "A null ValueSequence" should "be equals to other null ValueSequence" in {
    TextType.nullSequence should be(TextType.nullSequence)
    TextType.nullSequence.values should be(List())
  }

  "A ValueSequence (foo, bar)" should "be equals to same value sequence" in {
    TextType.sequenceOf(List("foo".toTextValue, "bar".toTextValue)) should
      be(TextType.sequenceOf(List("foo".toTextValue, "bar".toTextValue)))
  }

  it should "have values" in {
    TextType.sequenceOf(List("foo".toTextValue, "bar".toTextValue)).values should be(
      List("foo".toTextValue, "bar".toTextValue))
  }

  it should "contains bar" in {
    TextType.sequenceOf(List("foo".toTextValue, "bar".toTextValue)).contains("bar".toTextValue) should be(true)
  }

  it should "get bar" in {
    TextType.sequenceOf(List("foo".toTextValue, "bar".toTextValue)).get(1).get should be("bar".toTextValue)
  }

  it should "have size 2" in {
    TextType.sequenceOf(List("foo".toTextValue, "bar".toTextValue)).size should be(2)
  }

  it should "have length 6" in {
    TextType.sequenceOf(List("foo".toTextValue, "bar".toTextValue)).length should be(6)
  }


}
