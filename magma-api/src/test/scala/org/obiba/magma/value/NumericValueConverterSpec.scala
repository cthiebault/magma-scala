package org.obiba.magma.value

import org.obiba.magma.UnitSpec
import org.obiba.magma.value.JavaObjToValueConverters.StringConverters

class NumericValueConverterSpec extends UnitSpec {

  "An NumericValueConverter" should "be found for number type" in {
    ValueConverter.findConverter(DecimalType, IntegerType).get should be(NumericValueConverter)
    ValueConverter.findConverter(IntegerType, DecimalType).get should be(NumericValueConverter)
  }

  it should "not be found for non numeric types" in {
    ValueConverter.findConverter(IntegerType, DateType) should be('empty)
  }

  it should "convert int to decimal" in {
    NumericValueConverter.convert("10".toIntegerValue, DecimalType).get should be("10".toDecimalValue)
  }

  it should "support null value" in {
    NumericValueConverter.convert(IntegerType.nullValue, DecimalType).get should be(DecimalType.nullValue)
  }

  it should "support null sequence" in {
    NumericValueConverter.convert(IntegerType.nullSequence, DecimalType).get should be(DecimalType.nullSequence)
  }

  it should "convert decimal to int" in {
    NumericValueConverter.convert("10".toDecimalValue, IntegerType).get should be("10".toIntegerValue)
    NumericValueConverter.convert("10.1".toDecimalValue, IntegerType).get should be("10".toIntegerValue)
  }

}
