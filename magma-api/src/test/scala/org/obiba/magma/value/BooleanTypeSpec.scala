package org.obiba.magma.value

import java.lang.Boolean._

import org.obiba.magma.UnitSpec

class BooleanTypeSpec extends UnitSpec {

  "A BooleanType" should "be named" in {
    BooleanType.name should be("boolean")
  }

  it should "support true string input" in {
    BooleanType.valueOf("true").value.get should be(TRUE)
  }

  it should "support false string input" in {
    BooleanType.valueOf("false").value.get should be(FALSE)
  }

  it should "support boolean input" in {
    BooleanType.valueOf(FALSE).value.get should be(FALSE)
  }
  it should "support value input" in {
    BooleanType.valueOf(BooleanType.valueOf("true")).value.get should be(TRUE)
  }

  it should "throw exception for invalid boolean" in {
    a[IllegalArgumentException] should be thrownBy {
      BooleanType.valueOf("string")
    }
  }

  it should "sort values" in {
    BooleanType.compare(BooleanType.falseValue, BooleanType.trueValue) should be < 0
    BooleanType.compare(BooleanType.nullValue, BooleanType.trueValue) should be < 0
    BooleanType.compare(BooleanType.nullValue, BooleanType.nullValue) should be(0)
    BooleanType.compare(BooleanType.falseValue, BooleanType.falseValue) should be(0)
    BooleanType.compare(BooleanType.trueValue, BooleanType.trueValue) should be(0)
  }

}
