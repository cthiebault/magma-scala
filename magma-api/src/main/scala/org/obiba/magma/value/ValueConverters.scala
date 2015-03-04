package org.obiba.magma.value

// pimp my library :)
object ValueConverters {

  implicit class StringConverters(val str: String) extends AnyVal {

    def toTextValue: Value = TextType.valueOf(str).get

    def toBooleanValue: Value = BooleanType.valueOf(str).get

    def toDecimalValue: Value = DecimalType.valueOf(str).get

    def toIntegerValue: Value = IntegerType.valueOf(str).get

    def toDateValue: Value = DateType.valueOf(str).get

    def toDateTimeValue: Value = DateTimeType.valueOf(str).get

  }

}
