package org.obiba.magma.value

import org.obiba.magma.UnitSpec
import org.obiba.magma.value.JavaObjToValueConverters.StringConverters

class IdentityValueConverterSpec extends UnitSpec {

  "An IdentityValueConverter" should "be found for same type" in {
    ValueConverter.findConverter(DecimalType, DecimalType).get should be(IdentityValueConverter)
    ValueConverter.findConverter(DateType, DateType).get should be(IdentityValueConverter)
  }

  it should "not be found for different types" in {
    ValueConverter.findConverter(TextType, DateType).get should not be IdentityValueConverter
  }

  it should "convert value" in {
    val value: Value = "10".toDecimalValue
    IdentityValueConverter.convert(value, DecimalType).get should be theSameInstanceAs value
  }

}
