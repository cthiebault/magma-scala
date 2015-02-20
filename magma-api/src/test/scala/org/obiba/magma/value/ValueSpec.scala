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
  }

  "A ValueSequence (foo, bar)" should "be equals to same value sequence" in {
    TextType.sequenceOf(List(TextType.valueOf("foo"), TextType.valueOf("bar"))) should
      be(TextType.sequenceOf(List(TextType.valueOf("foo"), TextType.valueOf("bar"))))
  }

  it should "have size 2" in {
    TextType.sequenceOf(List(TextType.valueOf("foo"), TextType.valueOf("bar"))).size should be(2)
  }

  it should "have length 6" in {
    TextType.sequenceOf(List(TextType.valueOf("foo"), TextType.valueOf("bar"))).length should be(6)
  }



}
