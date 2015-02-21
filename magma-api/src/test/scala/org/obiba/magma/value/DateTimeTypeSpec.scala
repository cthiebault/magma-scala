package org.obiba.magma.value

import java.text.SimpleDateFormat
import java.time.format.{ResolverStyle, DateTimeFormatter}
import java.time.{Duration, Period, LocalDateTime, LocalDate}
import java.util.{Calendar, Date}

import org.obiba.magma.utils.DateConverters.{CalendarConverters, DateConverters, LocalDateConverters}
import org.obiba.magma.value.ValueConverters.StringConverters
import org.obiba.magma.{MagmaRuntimeException, UnitSpec}

class DateTimeTypeSpec extends UnitSpec {

  "A DateTimeType" should "be named" in {
    DateTimeType.name should be("datetime")
  }

  it should "have value for now()" in {
    DateTimeType.now.value should be('defined)
  }

  DateTimeType.SUPPORTED_FORMATS_PATTERNS.foreach(testPattern)

  it should "not support unknown pattern" in {
    val thrown = the[MagmaRuntimeException] thrownBy {
      DateTimeType.valueOf("1 janvier 2015")
    }
    thrown.getMessage should startWith("Cannot parse datetime from string value")
  }

  it should "have no value for null param" in {
    val nullValue: Value = DateTimeType.valueOf(null)
    nullValue.value should be(None)
    DateTimeType.toString(nullValue) should be(null)
  }

  it can "be built from Date" in {
    val now: Date = new Date
    DateTimeType.valueOf(now).value.get should be(now.toLocalDateTime)
  }

  it can "be built from Calendar" in {
    val calendar: Calendar = Calendar.getInstance
    DateTimeType.valueOf(calendar).value.get should be(calendar.toLocalDateTime)
  }

  it can "be built from Value" in {
    val now: Value = DateTimeType.now
    DateTimeType.valueOf(now).value.get should be(now.value.get)
    DateTimeType.valueOf(now) should be(now)
  }

  it should "sort values" in {
    DateTimeType.compare(DateTimeType.valueOf("2015/01/01 12:00"), DateTimeType.now) should be < 0
    DateTimeType.compare(DateTimeType.valueOf("2015/01/01 12:00"), DateTimeType.valueOf("2014/01/01 12:00")) should be > 0
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
