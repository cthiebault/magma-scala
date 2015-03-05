package org.obiba.magma.value

import java.time.format.{DateTimeFormatter, ResolverStyle}
import java.time.{LocalDate, LocalDateTime, LocalTime}
import java.util.{Base64, Calendar, Comparator, Date, Locale}

import com.google.common.base.Strings
import org.obiba.magma.utils.DateConverters.{CalendarConverters, DateConverters}
import org.obiba.magma.utils.StringUtils.StringsWrapper
import org.obiba.magma.value.ValueLoader.StaticValueLoader

import scala.util.Try

sealed trait ValueType extends Comparator[Value] {

  def name: String

  def nullValue: Value

  def valueOf(string: String): Option[Value]

  def valueOf(value: Any): Option[Value]

  def valueOf(valueLoader: ValueLoader): Option[Value]

  def toString(value: Value): String

  def nullSequence: ValueSequence

  def sequenceOf(values: Traversable[Value]): ValueSequence

  def convert(value: Value): Option[Value]

  /**
   * Returns true if this type represents a date, time or both.
   *
   * @return if this type represents a date, time or both.
   */
  def isDateTime: Boolean

  /**
   * Returns true if this type represents a number.
   *
   * @return true if this type represents a number
   */
  def isNumeric: Boolean

  /**
   * Returns true if this type represents a geolocalisation.
   *
   * @return true if this type represents a geolocalisation
   */
  def isGeo: Boolean

  /**
   * Returns true if this type represents a binary.
   *
   * @return true if this type represents a binary
   */
  def isBinary: Boolean

}

abstract class AbstractValueType extends ValueType {

  private val NULL_VALUE: Value = Value(this, new StaticValueLoader(null))
  private val NULL_VALUE_SEQUENCE: ValueSequence = sequenceOf(null)

  override def nullValue: Value = NULL_VALUE

  override def nullSequence: ValueSequence = NULL_VALUE_SEQUENCE

  override def toString(value: Value): String = {
    if (value == null) {
      null
    } else {
      value.value match {
        case Some(v) => toString(v)
        case None => null
      }
    }
  }

  protected def toString(value: Any): String = value.toString

  override def valueOf(valueLoader: ValueLoader): Option[Value] = Some(Value(this, valueLoader))

  override def sequenceOf(values: Traversable[Value]): ValueSequence = new ValueSequence(this, values)

  override def convert(value: Value): Option[Value] = {
    if (this == value.valueType) {
      Some(value)
    }
    else if (value.isNull) {
      value match {
        case s: ValueSequence => Some(nullSequence)
        case _ => Some(nullValue)
      }
    }
    else {
      ValueConverter.findConverter(value.valueType, this) match {
        case Some(c) => c.convert(value, this)
        case _ => None
      }
    }
  }

  override def compare(o1: Value, o2: Value): Int = ValueComparator.compare(o1, o2)

  override def isDateTime: Boolean = false

  override def isNumeric: Boolean = false

  override def isGeo: Boolean = false

  override def isBinary: Boolean = false

}

object TextType extends AbstractValueType {

  override def name: String = "text"

  override def valueOf(string: String): Option[Value] = Some(Value(this, new StaticValueLoader(string)))

  override def valueOf(value: Any): Option[Value] = {
    if (value == null) Some(nullValue) else valueOf(value.toString)
  }

  override def compare(o1: Value, o2: Value): Int = {
    if (o1.isNull && o2.isNull) return 0
    if (o1.isNull) return -1
    if (o2.isNull) return 1

    val s1: String = o1.value.get.asInstanceOf[String]
    val s2: String = o2.value.get.asInstanceOf[String]
    Strings.nullToEmpty(s1.trim).compareTo(Strings.nullToEmpty(s2))
  }
}

object BooleanType extends AbstractValueType {

  private val TRUE: Value = Value(this, new StaticValueLoader(true))
  private val FALSE: Value = Value(this, new StaticValueLoader(false))

  override def name: String = "boolean"

  override def valueOf(string: String): Option[Value] = {
    if (string.isNullOrEmpty) Some(nullValue) else valueOf(string.toBoolean)
  }

  override def valueOf(value: Any): Option[Value] = {
    value match {
      case null => Some(nullValue)
      case b: Boolean => Some(valueOf(b))
      case _ => valueOf(value.toString)
    }
  }

  def trueValue: Value = valueOf("true").get

  def falseValue: Value = valueOf("false").get

  private def valueOf(value: Boolean): Value = if (value) TRUE else FALSE

}

trait NumberType extends ValueType {
  override def isNumeric: Boolean = true
}

object DecimalType extends AbstractValueType with NumberType {

  override def name: String = "decimal"

  override def valueOf(string: String): Option[Value] = {
    if (string.isNullOrEmpty) Some(nullValue) else valueOf(string.replaceAll(",", ".").trim.toDouble)
  }

  override def valueOf(value: Any): Option[Value] = {
    value match {
      case null => Some(nullValue)
      case n: Number => Some(valueOf(n.doubleValue()))
      case _ => valueOf(value.toString)
    }
  }

  private def valueOf(value: Double): Value = {
    Value(this, new StaticValueLoader(value))
  }
}

object IntegerType extends AbstractValueType with NumberType {

  override def name: String = "integer"

  override def valueOf(string: String): Option[Value] = {
    if (string.isNullOrEmpty) Some(nullValue) else valueOf(string.replaceAll(",", ".").trim.toInt)
  }

  override def valueOf(value: Any): Option[Value] = {
    value match {
      case null => Some(nullValue)
      case n: Number => Some(valueOf(n.intValue()))
      case _ => valueOf(value.toString)
    }
  }

  private def valueOf(value: Int): Value = {
    Value(this, new StaticValueLoader(value))
  }
}

trait TemporalType extends ValueType {
  override def isDateTime: Boolean = true
}

object DateType extends AbstractValueType with TemporalType {

  protected[value] val SUPPORTED_FORMATS_PATTERNS = List(
    "yyyy-MM-dd", // preferred one: ISO_8601
    "yyyy/MM/dd",
    "yyyy.MM.dd",
    "yyyy MM dd",
    "dd-MM-yyyy",
    "dd/MM/yyyy",
    "dd.MM.yyyy",
    "dd MM yyyy")

  /**
   * Preferred format.
   */
  private val ISO_8601: DateTimeFormatter = formatter(SUPPORTED_FORMATS_PATTERNS.head)

  /**
   * These are used to support common date formats.
   */
  private val SUPPORTED_FORMATS: List[DateTimeFormatter] = SUPPORTED_FORMATS_PATTERNS.map(formatter)

  private def formatter(pattern: String): DateTimeFormatter = {
    DateTimeFormatter.ofPattern(pattern).withResolverStyle(ResolverStyle.LENIENT)
  }

  override def name: String = "date"

  override def valueOf(string: String): Option[Value] = {
    if (string == null) {
      Some(nullValue)
    } else {
      // TODO should not iterate on every format but return on the first success
      SUPPORTED_FORMATS
        .map(tryParsing(string, _))
        .filter(_.isSuccess)
        .map(_.get)
        .headOption
    }
  }

  private def tryParsing(string: String, format: DateTimeFormatter): Try[Value] = {
    Try(valueOf(LocalDate.parse(string, format)))
  }

  private def valueOf(localDate: LocalDate): Value = Value(this, new StaticValueLoader(localDate))

  override def valueOf(any: Any): Option[Value] = {
    any match {
      case null => Some(nullValue)
      case d: Date => Some(valueOf(d.toLocalDate))
      case ld: LocalDate => Some(valueOf(ld))
      case ldt: LocalDateTime => Some(valueOf(ldt.toLocalDate))
      case c: Calendar => Some(valueOf(c.toLocalDate))
      case s: String => valueOf(s)
      case v: Value => if (v.isNull) Some(nullValue) else valueOf(v.value.get)
      case _ => valueOf(any.toString)
    }
  }

  override protected def toString(value: Any): String = ISO_8601.format(value.asInstanceOf[LocalDate])

  def now: Value = valueOf(LocalDate.now)

  override def isDateTime: Boolean = true

}

object DateTimeType extends AbstractValueType with TemporalType {

  protected[value] val SUPPORTED_FORMATS_PATTERNS = List(
    "yyyy-MM-dd'T'HH:mm:ss.SSS", // preferred one: ISO_8601
    "yyyy-MM-dd'T'HH:mm:ss",
    "yyyy-MM-dd'T'HH:mm",
    "yyyy-MM-dd HH:mm:ss",
    "yyyy/MM/dd HH:mm:ss",
    "yyyy.MM.dd HH:mm:ss",
    "yyyy MM dd HH:mm:ss",
    "yyyy-MM-dd HH:mm",
    "yyyy/MM/dd HH:mm",
    "yyyy.MM.dd HH:mm",
    "yyyy MM dd HH:mm")

  /**
   * Preferred format.
   */
  protected[value] val ISO_8601: DateTimeFormatter = formatter(SUPPORTED_FORMATS_PATTERNS.head)

  /**
   * These are used to support common date formats.
   */
  private val SUPPORTED_FORMATS: List[DateTimeFormatter] = SUPPORTED_FORMATS_PATTERNS.map(formatter)

  private def formatter(pattern: String): DateTimeFormatter = {
    DateTimeFormatter.ofPattern(pattern).withResolverStyle(ResolverStyle.LENIENT)
  }

  override def name: String = "datetime"

  override def valueOf(string: String): Option[Value] = {
    if (string == null) {
      Some(nullValue)
    } else {
      // TODO should not iterate on every format but return on the first success
      SUPPORTED_FORMATS
        .map(tryParsing(string, _))
        .filter(_.isSuccess)
        .map(_.get)
        .headOption
    }
  }

  private def tryParsing(string: String, format: DateTimeFormatter): Try[Value] = {
    Try(valueOf(LocalDateTime.parse(string, format)))
  }

  private def valueOf(localDateTime: LocalDateTime): Value = Value(this, new StaticValueLoader(localDateTime))

  override def valueOf(any: Any): Option[Value] = {
    any match {
      case null => Some(nullValue)
      case d: Date => Some(valueOf(d.toLocalDateTime))
      case ld: LocalDate => Some(valueOf(LocalDateTime.of(ld, LocalTime.of(0, 0))))
      case ldt: LocalDateTime => Some(valueOf(ldt))
      case c: Calendar => Some(valueOf(c.toLocalDateTime))
      case s: String => valueOf(s)
      case v: Value => if (v.isNull) Some(nullValue) else valueOf(v.value.get)
      case _ => valueOf(any.toString)
    }
  }

  override protected def toString(value: Any): String = ISO_8601.format(value.asInstanceOf[LocalDateTime])

  def now: Value = valueOf(LocalDateTime.now)

}

object LocaleType extends AbstractValueType {

  override def name: String = "locale"

  override def valueOf(string: String): Option[Value] = {
    if (string.isNullOrEmpty) {
      Some(nullValue)
    } else {
      val parts = string.split("_")
      parts.length match {
        case 1 => Some(Value(this, new StaticValueLoader(new Locale(parts(0)))))
        case 2 => Some(Value(this, new StaticValueLoader(new Locale(parts(0), parts(1)))))
        case 3 => Some(Value(this, new StaticValueLoader(new Locale(parts(0), parts(1), parts(2)))))
        case _ => None
      }
    }
  }

  override def valueOf(value: Any): Option[Value] = {
    value match {
      case null => Some(nullValue)
      case l: Locale => Some(Value(this, new StaticValueLoader(l)))
      case s: String => valueOf(s)
      case _ => None
    }
  }

}

object BinaryType extends AbstractValueType {

  override def name: String = "binary"

  override def valueOf(string: String): Option[Value] = {
    if (string.isNullOrEmpty) {
      Some(nullValue)
    } else {
      Some(Value(this, new StaticValueLoader(Base64.getDecoder.decode(string))))
    }
  }

  override def valueOf(value: Any): Option[Value] = {
    value match {
      case null => Some(nullValue)
      case a: Array[Byte] => Some(Value(this, new StaticValueLoader(a)))
      case s: String => valueOf(s)
      case _ => None
    }
  }

  override def isBinary: Boolean = true

}

//object PointType extends AbstractValueType {
//  def name: String = "point"
//}

//object LineStringType extends AbstractValueType {
//  def name: String = "linestring"
//}

//object PolygonType extends AbstractValueType {
//  def name: String = "polygon"
//}

