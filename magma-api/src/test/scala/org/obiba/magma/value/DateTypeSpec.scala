package org.obiba.magma.value

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.{Calendar, Date}

import org.obiba.magma.{MagmaRuntimeException, UnitSpec}
import org.obiba.magma.utils.DateConverters.LocalDateConverters

class DateTypeSpec extends UnitSpec {

  "A DateType" should "be named" in {
    DateType.name should be("date")
  }

  it should "have value for now()" in {
    DateType.now.value should be('defined)
  }

  DateType.SUPPORTED_FORMATS_PATTERNS.foreach(testPattern)

  it should "not support unknown pattern" in {
    val thrown = the[MagmaRuntimeException] thrownBy {
      DateType.valueOf(format("yyyy", LocalDate.now))
    }
    thrown.getMessage should startWith("Cannot parse date from string value")
  }

  it should "have no value for null param" in {
    val nullValue: Value = DateType.valueOf(null)
    nullValue.value should be(None)
    DateType.toString(nullValue) should be(null)
  }

  it can "be built from Date" in {
    DateType.valueOf(new Date()).value.get should be(LocalDate.now)
  }

  it can "be built from Calendar" in {
    DateType.valueOf(Calendar.getInstance()).value.get should be(LocalDate.now)
  }
  
  it can "be built from Value" in {
    DateType.valueOf(DateType.now).value.get should be(LocalDate.now)
  }

  private def testPattern(pattern: String): Unit = {
    it should s"support $pattern format" in {
      val now = LocalDate.now
      val value: Value = DateType.valueOf(format(pattern, now))
      value.value should be('defined)
      value.value.get should be(now)
      DateType.toString(value) should be(now.toString)
    }
  }

  private def format(pattern: String, date: LocalDate): String = new SimpleDateFormat(pattern).format(date.toDate)
}
