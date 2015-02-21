package org.obiba.magma.value

import java.time.format.{DateTimeFormatter, DateTimeParseException, ResolverStyle}
import java.time.{LocalDate, LocalDateTime}
import java.util.{Calendar, Comparator, Date}

import com.google.common.base.Strings
import org.obiba.magma.MagmaRuntimeException
import org.obiba.magma.logging.Slf4jLogging
import org.obiba.magma.utils.DateConverters.{CalendarConverters, DateConverters}
import org.obiba.magma.utils.StringUtils.StringsWrapper
import org.obiba.magma.value.ValueLoader.StaticValueLoader

sealed trait ValueType extends Comparator[Value] {

  def name: String

  def nullValue: Value

  def valueOf(string: String): Value

  def valueOf(value: Any): Value

  def valueOf(valueLoader: ValueLoader): Value

  def toString(value: Value): String

  def nullSequence: ValueSequence

  def sequenceOf(values: Traversable[Value]): ValueSequence

  //    def convert(value: Value): Value
}


abstract class AbstractValueType extends ValueType {

  def nullValue: Value = valueOf(null)

  def nullSequence: ValueSequence = sequenceOf(null)

  def toString(value: Value): String = {
    if (value == null) return null
    value.value match {
      case Some(v) => toString(v)
      case None => null
    }
  }

  protected def toString(value: Any): String = value.toString

  def valueOf(valueLoader: ValueLoader): Value = Value(this, valueLoader)

  def sequenceOf(values: Traversable[Value]): ValueSequence = new ValueSequence(this, values)

  override def compare(o1: Value, o2: Value): Int = ValueComparator.compare(o1, o2)

}


object TextType extends AbstractValueType {

  def name: String = "text"

  def valueOf(string: String): Value = Value(this, new StaticValueLoader(string))

  def valueOf(value: Any): Value = {
    if (value == null) nullValue else valueOf(value.toString)
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

  def name: String = "boolean"

  def valueOf(string: String): Value = {
    if (string.isNullOrEmpty) nullValue else valueOf(string.toBoolean)
  }

  def valueOf(value: Any): Value = {
    if (value == null) return nullValue
    value match {
      case b: Boolean => valueOf(b)
      case _ => valueOf(value.toString)
    }
  }

  def trueValue: Value = valueOf("true")

  def falseValue: Value = valueOf("false")

  private def valueOf(value: Boolean): Value = if (value) TRUE else FALSE

}


trait NumberType extends ValueType

object DecimalType extends AbstractValueType with NumberType {

  def name: String = "decimal"

  def valueOf(string: String): Value = {
    if (string.isNullOrEmpty) nullValue else valueOf(string.replaceAll(",", ".").trim.toDouble)
  }

  def valueOf(value: Any): Value = {
    if (value == null) return nullValue
    value match {
      case n: Number => valueOf(n.doubleValue())
      case _ => valueOf(value.toString)
    }
  }

  private def valueOf(value: Double): Value = {
    Value(this, new StaticValueLoader(value))
  }
}


object IntegerType extends AbstractValueType with NumberType {

  def name: String = "integer"

  def valueOf(string: String): Value = {
    if (string.isNullOrEmpty) nullValue else valueOf(string.replaceAll(",", ".").trim.toInt)
  }

  def valueOf(value: Any): Value = {
    if (value == null) return nullValue
    value match {
      case n: Number => valueOf(n.intValue())
      case _ => valueOf(value.toString)
    }
  }

  private def valueOf(value: Int): Value = {
    Value(this, new StaticValueLoader(value))
  }
}


object DateType extends AbstractValueType with Slf4jLogging {

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

  private val SUPPORTED_FORMATS_PATTERN: String = SUPPORTED_FORMATS_PATTERNS.mkString(", ")

  private def formatter(pattern: String): DateTimeFormatter = {
    DateTimeFormatter.ofPattern(pattern).withResolverStyle(ResolverStyle.LENIENT)
  }

  override def name: String = "date"

  override def valueOf(string: String): Value = {
    if (string == null) {
      return nullValue
    }
    for (format <- SUPPORTED_FORMATS) {
      try {
        return valueOf(LocalDate.parse(string, format))
      }
      catch {
        case e: DateTimeParseException => {}
      }
    }
    throw new MagmaRuntimeException(
      s"Cannot parse date from string value '$string'. Expected format is one of $SUPPORTED_FORMATS_PATTERN")
  }

  private def valueOf(localDate: LocalDate): Value = Value(this, new StaticValueLoader(localDate))

  override def valueOf(any: Any): Value = {
    any match {
      case null => nullValue
      case d: Date => valueOf(d.toLocalDate)
      case c: Calendar => valueOf(c.toLocalDate)
      case s: String => valueOf(s)
      case v: Value => if (v.isNull) nullValue else valueOf(v.value.get)
      case _ => valueOf(any.toString)
    }
  }

  override protected def toString(value: Any): String = ISO_8601.format(value.asInstanceOf[LocalDate])

  def now: Value = valueOf(LocalDate.now)

}

object DateTimeType extends AbstractValueType with Slf4jLogging {

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

  private val SUPPORTED_FORMATS_PATTERN: String = SUPPORTED_FORMATS_PATTERNS.mkString(", ")

  private def formatter(pattern: String): DateTimeFormatter = {
    DateTimeFormatter.ofPattern(pattern).withResolverStyle(ResolverStyle.LENIENT)
  }

  override def name: String = "datetime"

  override def valueOf(string: String): Value = {
    if (string == null) {
      return nullValue
    }
    for (format <- SUPPORTED_FORMATS) {
      try {
        return valueOf(LocalDateTime.parse(string, format))
      }
      catch {
        case e: DateTimeParseException => {}
      }
    }
    throw new MagmaRuntimeException(
      s"Cannot parse datetime from string value '$string'. Expected format is one of $SUPPORTED_FORMATS_PATTERN")
  }

  private def valueOf(localDateTime: LocalDateTime): Value = Value(this, new StaticValueLoader(localDateTime))

  override def valueOf(any: Any): Value = {
    any match {
      case null => nullValue
      case d: Date => valueOf(d.toLocalDateTime)
      case c: Calendar => valueOf(c.toLocalDateTime)
      case s: String => valueOf(s)
      case v: Value => if (v.isNull) nullValue else valueOf(v.value.get)
      case _ => valueOf(any.toString)
    }
  }

  override protected def toString(value: Any): String = ISO_8601.format(value.asInstanceOf[LocalDateTime])

  def now: Value = valueOf(LocalDateTime.now)

}

//    case class LocaleType(name: String = "locale") extends ValueType
//
//    case class BinaryType(name: String = "binary") extends ValueType
//
//    case class PointType(name: String = "point") extends ValueType
//
//    case class LineStringType(name: String = "linestring") extends ValueType
//
//    case class PolygonType(name: String = "polygon") extends ValueType
