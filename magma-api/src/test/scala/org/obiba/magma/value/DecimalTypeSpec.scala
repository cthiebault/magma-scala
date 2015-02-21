package org.obiba.magma.value

import org.obiba.magma.UnitSpec

class DecimalTypeSpec extends UnitSpec {

  "A DecimalType" should "be named" in {
    DecimalType.name should be("decimal")
  }
  
  it should "support string input" in {
    DecimalType.valueOf("10").value.get should be(10)
  }

  it should "support number input" in {
    DecimalType.valueOf(10d).value.get should be(10d)
  }

  it should "support value input" in {
    DecimalType.valueOf(IntegerType.valueOf(10)).value.get should be(10)
  }

  it should "have no value for null param" in {
    DecimalType.valueOf(null).value should be('empty)
  }
  
  it should "be equals to toString()" in {
    DecimalType.toString(IntegerType.valueOf(10)) should be("10")
  }
  
  it should "throw exception for invalid number" in {
    a[NumberFormatException] should be thrownBy {
      DecimalType.valueOf("string")
    }
  }

  it should "sort values" in {
    DecimalType.compare(DecimalType.valueOf("1"), DecimalType.valueOf("2")) should be < 0
    DecimalType.compare(DecimalType.valueOf("2"), DecimalType.valueOf("1")) should be > 0
    DecimalType.compare(DecimalType.nullValue, DecimalType.valueOf("2")) should be < 0
    DecimalType.compare(DecimalType.nullValue, DecimalType.nullValue) should be(0)
    DecimalType.compare(DecimalType.valueOf("2"), DecimalType.valueOf("2")) should be(0)
  }

}
