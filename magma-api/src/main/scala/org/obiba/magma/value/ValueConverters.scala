package org.obiba.magma.value

// pimp my library :)
object ValueConverters {

  implicit class StringConverters(val str: String) extends AnyVal {

    def toTextValue: Value = TextType.valueOf(str)

    def toBooleanValue: Value = BooleanType.valueOf(str)

    def toDecimalValue: Value = DecimalType.valueOf(str)

    def toIntegerValue: Value = IntegerType.valueOf(str)

    def toDateValue: Value = DateType.valueOf(str)

    def toDateTimeValue: Value = DateTimeType.valueOf(str)

  }

}
