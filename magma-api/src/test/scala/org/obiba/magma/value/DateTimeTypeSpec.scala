package org.obiba.magma.value

import java.time.format.{DateTimeFormatter, ResolverStyle}
import java.time.{Duration, LocalDateTime}
import java.util.{Calendar, Date}

import org.obiba.magma.UnitSpec
import org.obiba.magma.utils.DateConverters.{CalendarConverters, DateConverters}
import org.obiba.magma.value.JavaObjToValueConverters.StringConverters

class DateTimeTypeSpec extends UnitSpec {

  "A DateTimeType" should "be named" in {
    DateTimeType.name should be("datetime")
  }

  it should "have value for now()" in {
    DateTimeType.now.value should be('defined)
  }

  DateTimeType.SUPPORTED_FORMATS_PATTERNS.foreach(testPattern)

  it should "not support unknown pattern" in {
    DateTimeType.valueOf("1 janvier 2015") should be('empty)
  }

  it should "have no value for null param" in {
    val nullValue: Value = DateTimeType.valueOf(null).get
    nullValue.value should be(None)
    DateTimeType.toString(nullValue) should be(null)
  }

  it can "be built from Date" in {
    val now: Date = new Date
    DateTimeType.valueOf(now).get.value.get should be(now.toLocalDateTime)
  }

  it can "be built from Calendar" in {
    val calendar: Calendar = Calendar.getInstance
    DateTimeType.valueOf(calendar).get.value.get should be(calendar.toLocalDateTime)
  }

  it can "be built from Value" in {
    val now: Value = DateTimeType.now
    DateTimeType.valueOf(now).get.value.get should be(now.value.get)
    DateTimeType.valueOf(now).get should be(now)
  }

  it should "sort values" in {
    DateTimeType.compare(DateTimeType.valueOf("2015/01/01 12:00").get, DateTimeType.now) should be < 0
    DateTimeType.compare(DateTimeType.valueOf("2015/01/01 12:00").get, DateTimeType.valueOf("2014/01/01 12:00").get) should be > 0
    DateTimeType.compare(DateTimeType.nullValue, DateTimeType.now) should be < 0
    DateTimeType.compare(DateTimeType.now, DateTimeType.now) should be(0)
    DateTimeType.compare(DateTimeType.nullValue, DateTimeType.nullValue) should be(0)
  }

  private def testPattern(pattern: String): Unit = {
    it should s"support $pattern format" in {
      val now = LocalDateTime.now

      val formattedDate: String = now.format(DateTimeFormatter.ofPattern(pattern).withResolverStyle(ResolverStyle.LENIENT))
      val value: Value = formattedDate.toDateTimeValue
      value.value should be('defined)

      val dateTimeValue: LocalDateTime = value.value.get.asInstanceOf[LocalDateTime]
      Duration.between(now, dateTimeValue).getSeconds should be < 1l

      // TODO test toString
      // DateTimeType.toString(value) should be(DateTimeType.ISO_8601.format(now))
    }
  }

}
