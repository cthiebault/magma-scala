package org.obiba.magma.value

import org.obiba.magma.UnitSpec
import org.obiba.magma.value.JavaObjToValueConverters.StringConverters

class TextToNumericValueConverterSpec extends UnitSpec {

  "An TextToNumericValueConverter" should "be found for number type" in {
    ValueConverter.findConverter(TextType, IntegerType).get should be(TextToNumericValueConverter)
    ValueConverter.findConverter(TextType, DecimalType).get should be(TextToNumericValueConverter)
  }

  it should "not be found for non numeric types" in {
    ValueConverter.findConverter(TextType, DateType) should not be TextToNumericValueConverter
  }

  it should "convert to decimal" in {
    TextToNumericValueConverter.convert("10".toTextValue, DecimalType).get should be("10".toDecimalValue)
  }

  it should "convert to int" in {
    TextToNumericValueConverter.convert("10".toTextValue, IntegerType).get should be("10".toIntegerValue)
  }

  it should "support null value" in {
    TextToNumericValueConverter.convert(TextType.nullValue, IntegerType).get should be(IntegerType.nullValue)
    TextToNumericValueConverter.convert(TextType.nullValue, DecimalType).get should be(DecimalType.nullValue)
  }

  it should "support null sequence" in {
    TextToNumericValueConverter.convert(TextType.nullSequence, IntegerType).get should be(IntegerType.nullSequence)
    TextToNumericValueConverter.convert(TextType.nullSequence, DecimalType).get should be(DecimalType.nullSequence)
  }

}
