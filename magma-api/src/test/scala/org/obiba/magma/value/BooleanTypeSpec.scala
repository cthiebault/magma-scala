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

  //  it should "support value input" in {
  //    BooleanType.valueOf(true).value.get should be(true)
  //  }
  //
  //  it should "have no value for null param" in {
  //    IntegerType.valueOf(null).value should be('empty)
  //  }
  //
  //  it should "be equals to toString()" in {
  //    IntegerType.toString(IntegerType.valueOf(10)) should be("10")
  //  }
  //
  //  it should "throw exception for invalid number" in {
  //    a[NumberFormatException] should be thrownBy {
  //      IntegerType.valueOf("string")
  //    }
  //  }

}
