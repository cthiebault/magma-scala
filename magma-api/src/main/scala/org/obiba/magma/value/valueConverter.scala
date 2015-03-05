package org.obiba.magma.value

import java.math.BigDecimal
import java.time.{LocalDate, LocalDateTime}

/**
 * Contract for converting {@code Value} instances from one {@code ValueType} to another.
 * <p/>
 * The {@code converts} methods will be invoked to determine if this {@code ValueConverter} can handle the conversion
 * from one type to another. If so, its {@code convert()} method is invoked. Implementations should not convert
 * {@code ValueSequence} instances. These are handled externally, and each value in a sequence will be converted using
 * the {@code convert()} method.
 */
sealed trait ValueConverter {

  /**
   * Returns true when this instance can convert from value instances of type {@code from} to value instances of type
   * {@code to}
   *
   * @param from the { @code ValueType} the original { @code Value} instance has
   * @param to the { @code ValueType} the resulting { @code Value} instance has
   * @return true when this converter can handle the conversion
   */
  def converts(from: ValueType, to: ValueType): Boolean

  /**
   * Converts the given {@code Value} instance to the {@code ValueType} {@code to}
   *
   * @param value the { @code Value} instance to convert.
   * @param to the { @code ValueType} to convert to
   * @return a { @code Value} instance of { @code ValueType} { @code to}
   */
  def convert(value: Value, to: ValueType): Option[Value]

}

object ValueConverter {
  def values(): Traversable[ValueConverter] = {
    // order is important
    List(
      IdentityValueConverter,
      NumericValueConverter,
      DatetimeValueConverter,
      AnyToTextValueConverter,
      TextToNumericValueConverter,
      TextToAnyTypeValueConverter
    )
  }

  def findConverter(from: ValueType, to: ValueType): Option[ValueConverter] = values().find(_.converts(from, to))

}

object IdentityValueConverter extends ValueConverter {

  override def converts(from: ValueType, to: ValueType): Boolean = from eq to

  override def convert(value: Value, to: ValueType): Option[Value] = Some(value)

}

object NumericValueConverter extends ValueConverter {

  override def converts(from: ValueType, to: ValueType): Boolean = {
    from.isNumeric && to.isNumeric
  }

  override def convert(value: Value, to: ValueType): Option[Value] = {
    // When converting decimal to integer, this will truncate the decimal places: 0.9 -> 0
    if (value.isNull) {
      Some(to.nullValue)
    } else {
      to.valueOf(value.value.get)
    }
  }

}

object DatetimeValueConverter extends ValueConverter {

  override def converts(from: ValueType, to: ValueType): Boolean = {
    from.isDateTime && to.isDateTime
  }

  override def convert(value: Value, to: ValueType): Option[Value] = {
    if (value.isNull) {
      Some(to.nullValue)
    } else {
      to match {
        case DateType => to.valueOf(value.value.get.asInstanceOf[LocalDateTime])
        case DateTimeType => to.valueOf(value.value.get.asInstanceOf[LocalDate])
        case _ => None
      }
    }
  }
}

object AnyToTextValueConverter extends ValueConverter {

  override def converts(from: ValueType, to: ValueType): Boolean = to eq TextType

  override def convert(value: Value, to: ValueType): Option[Value] = TextType.valueOf(value.toString)

}

object TextToAnyTypeValueConverter extends ValueConverter {

  override def converts(from: ValueType, to: ValueType): Boolean = from eq TextType

  override def convert(value: Value, to: ValueType): Option[Value] = to.valueOf(value)

}

object TextToNumericValueConverter extends ValueConverter {

  override def converts(from: ValueType, to: ValueType): Boolean = {
    (from eq TextType) && to.isNumeric
  }

  override def convert(value: Value, to: ValueType): Option[Value] = {
    if (value.isNull) {
      Some(to.nullValue)
    } else {
      to.valueOf(new BigDecimal(value.toString))
    }
  }

}
