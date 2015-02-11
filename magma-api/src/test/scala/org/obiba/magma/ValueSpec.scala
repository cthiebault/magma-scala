package org.obiba.magma


class ValueSpec extends UnitSpec {

  "A Value" should "be equals to same value" in {
    TextType.valueOf("test") should be(TextType.valueOf("test"))
  }
  it should "have the right length for non null value" in {
    TextType.valueOf("test").getLength should be(4)
  }
  it should "have 0 length for null value" in {
    TextType.nullValue.getLength should be(0)
  }

  "A null ValueSequence" should "be equals to other null ValueSequence" in {
    TextType.nullSequence should be(TextType.nullSequence)
  }

  "A ValueSequence (foo, bar)" should "be equals to same value sequence" in {
    TextType.sequenceOf(List(TextType.valueOf("foo"), TextType.valueOf("bar"))) should
        be(TextType.sequenceOf(List(TextType.valueOf("foo"), TextType.valueOf("bar"))))
  }
  it should "have size 2" in {
    TextType.sequenceOf(List(TextType.valueOf("foo"), TextType.valueOf("bar"))).getSize should be(2)
  }
  it should "have length 6" in {
    TextType.sequenceOf(List(TextType.valueOf("foo"), TextType.valueOf("bar"))).getLength should be(6)
  }

}
