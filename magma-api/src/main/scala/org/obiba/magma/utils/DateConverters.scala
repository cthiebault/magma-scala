package org.obiba.magma.utils

import java.text.SimpleDateFormat
import java.time.{ZoneId, LocalDate}
import java.util.Date

object DateConverters {

  implicit class LocalDateConverters(val ld: LocalDate) extends AnyVal {
    def toDate: Date = Date.from(ld.atStartOfDay.atZone(ZoneId.systemDefault).toInstant)
  }

  implicit class DateConverters(val str: String) extends AnyVal {
    def toNow: String = new SimpleDateFormat(str).format(new Date())
  }

}
