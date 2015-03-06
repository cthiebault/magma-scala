package org.obiba.magma.value

import org.obiba.magma.UnitSpec
import org.obiba.magma.value.JavaObjToValueConverters.StringConverters

class AnyToTextValueConverterSpec extends UnitSpec {

  "An AnyToTextValueConverter" should "be found for all types but TextType" in {
    ValueType.types()
      .filter(_ ne TextType) // !(_ eq TextType)
      .foreach(ValueConverter.findConverter(_, TextType).get should be(AnyToTextValueConverter))
  }

  it should "convert from decimal" in {
    AnyToTextValueConverter.convert("10".toDecimalValue, TextType).get should be("10.0".toTextValue)
  }

  it should "convert from int" in {
    AnyToTextValueConverter.convert("10".toIntegerValue, TextType).get should be("10".toTextValue)
  }

  it should "convert from boolean" in {
    AnyToTextValueConverter.convert(BooleanType.falseValue, TextType).get should be("false".toTextValue)
  }

  it should "convert from date" in {
    AnyToTextValueConverter.convert("2015/01/01".toDateValue, TextType).get should be("2015-01-01".toTextValue)
  }

  it should "convert from datetime" in {
    AnyToTextValueConverter.convert("2015/01/01 12:00:00".toDateTimeValue, TextType).get should
      be("2015-01-01T12:00:00.000".toTextValue)
  }

  it should "support null value" in {
    AnyToTextValueConverter.convert(IntegerType.nullValue, TextType).get should be(TextType.nullValue)
    AnyToTextValueConverter.convert(DecimalType.nullValue, TextType).get should be(TextType.nullValue)
    AnyToTextValueConverter.convert(BooleanType.nullValue, TextType).get should be(TextType.nullValue)
  }

  it should "support null sequence" in {
    AnyToTextValueConverter.convert(IntegerType.nullSequence, TextType).get should be(TextType.nullSequence)
    AnyToTextValueConverter.convert(DecimalType.nullSequence, TextType).get should be(TextType.nullSequence)
    AnyToTextValueConverter.convert(BooleanType.nullSequence, TextType).get should be(TextType.nullSequence)
  }

}
