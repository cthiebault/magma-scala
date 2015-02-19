package org.obiba.magma.value

import java.text.SimpleDateFormat
import java.time.LocalDate

import org.obiba.magma.utils.DateConverters.LocalDateConverters
import org.scalatest._

class DateTypeSpec extends FlatSpec with Matchers {

  "A DateType" should "be named" in {
    DateType.name should be("date")
  }
  it should "have value for now()" in {
    DateType.now.value should be('defined)
  }
  DateType.SUPPORTED_FORMATS_PATTERNS.foreach(v)

  it should "not support unknown pattern" in {
    val now = LocalDate.now()
    DateType.valueOf(format("yyyy", now)).value.get should be(now)
  }

  private def v(pattern: String): Unit = {
    it should s"support $pattern format" in {
      val now = LocalDate.now()
      DateType.valueOf(format(pattern, now)).value.get should be(now)
    }
  }

  private def format(pattern: String, date: LocalDate): String = new SimpleDateFormat(pattern).format(date.toDate)
}
