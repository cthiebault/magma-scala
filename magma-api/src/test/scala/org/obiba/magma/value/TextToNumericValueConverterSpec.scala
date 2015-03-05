package org.obiba.magma.value

import org.obiba.magma.UnitSpec
import org.obiba.magma.value.JavaObjToValueConverters.StringConverters
import org.obiba.magma.value.TextToNumericValueConverter

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

}
