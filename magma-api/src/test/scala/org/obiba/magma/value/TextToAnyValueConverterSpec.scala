package org.obiba.magma.value

import org.obiba.magma.UnitSpec
import org.obiba.magma.value.JavaObjToValueConverters.StringConverters

class TextToAnyValueConverterSpec extends UnitSpec {

  "An TextToAnyValueConverter" should "be found for all types but TextType" in {
    ValueType.types()
      .filter(_ ne TextType) // !(_ eq TextType)
      .filterNot(_.isNumeric)
      .foreach(ValueConverter.findConverter(TextType, _).get should be(TextToAnyValueConverter))
  }

  it should "convert to decimal" in {
    TextToAnyValueConverter.convert("10.0".toTextValue, DecimalType).get should be("10".toDecimalValue)
  }

  it should "convert to int" in {
    TextToAnyValueConverter.convert("10".toTextValue, IntegerType).get should be("10".toIntegerValue)
  }

  it should "convert to boolean" in {
    TextToAnyValueConverter.convert("false".toTextValue, BooleanType).get should be(BooleanType.falseValue)
  }

  it should "convert to date" in {
    TextToAnyValueConverter.convert("2015-01-01".toTextValue, DateType).get should be("2015/01/01".toDateValue)
  }

  it should "convert to datetime" in {
    TextToAnyValueConverter.convert("2015-01-01T12:00:00.000".toTextValue, DateTimeType).get should
      be("2015/01/01 12:00:00".toDateTimeValue)
  }

  it should "support null value" in {
    TextToAnyValueConverter.convert(TextType.nullValue, IntegerType).get should be(IntegerType.nullValue)
    TextToAnyValueConverter.convert(TextType.nullValue, DecimalType).get should be(DecimalType.nullValue)
    TextToAnyValueConverter.convert(TextType.nullValue, BooleanType).get should be(BooleanType.nullValue)
    TextToAnyValueConverter.convert(TextType.nullValue, DateTimeType).get should be(DateTimeType.nullValue)
    TextToAnyValueConverter.convert(TextType.nullValue, DateType).get should be(DateType.nullValue)
  }

  it should "support null sequence" in {
    TextToAnyValueConverter.convert(TextType.nullSequence, IntegerType).get should be(IntegerType.nullSequence)
    TextToAnyValueConverter.convert(TextType.nullSequence, DecimalType).get should be(DecimalType.nullSequence)
    TextToAnyValueConverter.convert(TextType.nullSequence, BooleanType).get should be(BooleanType.nullSequence)
    TextToAnyValueConverter.convert(TextType.nullSequence, DateTimeType).get should be(DateTimeType.nullSequence)
    TextToAnyValueConverter.convert(TextType.nullSequence, DateType).get should be(DateType.nullSequence)
  }

}
