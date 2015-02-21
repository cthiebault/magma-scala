package org.obiba.magma.utils

import java.time.{LocalDate, LocalDateTime, ZoneId}
import java.util.{Calendar, Date}

object DateConverters {

  implicit class LocalDateConverters(val ld: LocalDate) extends AnyVal {
    
    def toDate: Date = Date.from(ld.atStartOfDay.atZone(ZoneId.systemDefault).toInstant)
    
  }

  implicit class DateConverters(val date: Date) extends AnyVal {
    
    def toLocalDate: LocalDate = date.toInstant.atZone(ZoneId.systemDefault).toLocalDate

    def toLocalDateTime: LocalDateTime = date.toInstant.atZone(ZoneId.systemDefault).toLocalDateTime
    
  }

  implicit class CalendarConverters(val calendar: Calendar) extends AnyVal {

    def toLocalDate: LocalDate = calendar.toInstant.atZone(ZoneId.systemDefault).toLocalDate

    def toLocalDateTime: LocalDateTime = calendar.toInstant.atZone(ZoneId.systemDefault).toLocalDateTime

  }


}
