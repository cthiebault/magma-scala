package org.obiba.magma.value

import org.obiba.magma.UnitSpec
import org.obiba.magma.value.ValueConverters.StringConverters

class DecimalTypeSpec extends UnitSpec {

  "A DecimalType" should "be named" in {
    DecimalType.name should be("decimal")
  }
  
  it should "support string input" in {
    DecimalType.valueOf("10").get.value.get should be(10)
    "10".toDecimalValue.value.get should be(10)
  }

  it should "support number input" in {
    DecimalType.valueOf(10d).get.value.get should be(10d)
  }

  it should "support value input" in {
    DecimalType.valueOf(IntegerType.valueOf(10).get).get.value.get should be(10)
  }

  it should "have no value for null param" in {
    DecimalType.valueOf(null).get.value should be('empty)
  }
  
  it should "be equals to toString()" in {
    DecimalType.toString(IntegerType.valueOf(10).get) should be("10")
  }
  
  it should "throw exception for invalid number" in {
    a[NumberFormatException] should be thrownBy {
      DecimalType.valueOf("string")
    }
  }

  it should "sort values" in {
    DecimalType.compare(DecimalType.valueOf("1").get, DecimalType.valueOf("2").get) should be < 0
    DecimalType.compare(DecimalType.valueOf("2").get, DecimalType.valueOf("1").get) should be > 0
    DecimalType.compare(DecimalType.nullValue, DecimalType.valueOf("2").get) should be < 0
    DecimalType.compare(DecimalType.nullValue, DecimalType.nullValue) should be(0)
    DecimalType.compare(DecimalType.valueOf("2").get, DecimalType.valueOf("2").get) should be(0)
  }

}
