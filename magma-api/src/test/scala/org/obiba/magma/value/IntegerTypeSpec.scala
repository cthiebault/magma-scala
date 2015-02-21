package org.obiba.magma.value

import org.obiba.magma.UnitSpec
import org.obiba.magma.value.ValueConverters.StringConverters

class IntegerTypeSpec extends UnitSpec {

  "A IntegerType" should "be named" in {
    IntegerType.name should be("integer")
  }
  
  it should "support string input" in {
    IntegerType.valueOf("10").value.get should be(10)
    "10".toIntegerValue.value.get should be(10)
  }

  it should "support number input" in {
    IntegerType.valueOf(10).value.get should be(10)
  }

  it should "support value input" in {
    IntegerType.valueOf(IntegerType.valueOf(10)).value.get should be(10)
  }

  it should "have no value for null param" in {
    IntegerType.valueOf(null).value should be('empty)
  }
  
  it should "be equals to toString()" in {
    IntegerType.toString(IntegerType.valueOf(10)) should be("10")
  }
  
  it should "throw exception for invalid number" in {
    a[NumberFormatException] should be thrownBy {
      IntegerType.valueOf("string")
    }
  }

  it should "sort values" in {
    IntegerType.compare(IntegerType.valueOf("1"), IntegerType.valueOf("2")) should be < 0
    IntegerType.compare(IntegerType.valueOf("2"), IntegerType.valueOf("1")) should be > 0
    IntegerType.compare(IntegerType.nullValue, IntegerType.valueOf("2")) should be < 0
    IntegerType.compare(IntegerType.nullValue, IntegerType.nullValue) should be(0)
    IntegerType.compare(IntegerType.valueOf("2"), IntegerType.valueOf("2")) should be(0)
  }
  
}
