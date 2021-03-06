package org.obiba.magma.value

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.{Calendar, Date}

import org.obiba.magma.value.JavaObjToValueConverters.StringConverters
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
    DateType.valueOf("1 janvier 2015") should be('empty)
  }

  it should "have no value for null param" in {
    val nullValue: Value = DateType.valueOf(null).get
    nullValue.value should be(None)
    DateType.toString(nullValue) should be(null)
  }

  it can "be built from Date" in {
    DateType.valueOf(new Date()).get.value.get should be(LocalDate.now)
  }

  it can "be built from Calendar" in {
    DateType.valueOf(Calendar.getInstance()).get.value.get should be(LocalDate.now)
  }
  
  it can "be built from Value" in {
    DateType.valueOf(DateType.now).get.value.get should be(LocalDate.now)
  }

  it should "sort values" in {
    DateType.compare(DateType.valueOf("2015/01/01").get, DateType.now) should be < 0
    DateType.compare(DateType.valueOf("2015/01/01").get, DateType.valueOf("2014/01/01").get) should be > 0
    DateType.compare(DateType.nullValue, DateType.now) should be < 0
    DateType.compare(DateType.now, DateType.now) should be(0)
    DateType.compare(DateType.nullValue, DateType.nullValue) should be(0)
  }

  private def testPattern(pattern: String): Unit = {
    it should s"support $pattern format" in {
      val now = LocalDate.now
      val formattedDate: String = format(pattern, now)
      val value: Value = formattedDate.toDateValue
      value.value should be('defined)
      value.value.get should be(now)
      DateType.toString(value) should be(now.toString)
    }
  }

  private def format(pattern: String, date: LocalDate): String = new SimpleDateFormat(pattern).format(date.toDate)
}
