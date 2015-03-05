package org.obiba.magma.value

import java.time.{LocalDate, LocalDateTime}

import org.obiba.magma.UnitSpec
import org.obiba.magma.value.JavaObjToValueConverters.StringConverters

class DatetimeValueConverterSpec extends UnitSpec {

  "An DatetimeValueConverter" should "be found for datetime type" in {
    ValueConverter.findConverter(DateType, DateTimeType).get should be(DatetimeValueConverter)
    ValueConverter.findConverter(DateTimeType, DateType).get should be(DatetimeValueConverter)
  }

  it should "not be found for non datetime types" in {
    ValueConverter.findConverter(DateTimeType, TextType) should not be DatetimeValueConverter
  }

  it should "convert to datetime" in {
    val value: Value = DatetimeValueConverter.convert("2015/01/01".toDateValue, DateTimeType).get
    val localDateTime: LocalDateTime = value.value.get.asInstanceOf[LocalDateTime]
    localDateTime.getYear should be(2015)
    localDateTime.getMonthValue should be(1)
    localDateTime.getDayOfMonth should be(1)
  }

  it should "convert to date" in {
    val value: Value = DatetimeValueConverter.convert("2015/01/01 12:00:00".toDateTimeValue, DateType).get
    val localDate: LocalDate = value.value.get.asInstanceOf[LocalDate]
    localDate.getYear should be(2015)
    localDate.getMonthValue should be(1)
    localDate.getDayOfMonth should be(1)
  }

}
