package org.obiba.magma.value

import org.obiba.magma.value.ValueLoader.StaticValueLoader

sealed trait ValueType {

  def name: String

  def nullValue: Value

  def valueOf(string: String): Value

  def valueOf(value: Any): Value

  def valueOf(valueLoader: ValueLoader): Value

  def toString(value: Value): String

  def nullSequence: ValueSequence

  def sequenceOf(values: Traversable[Value]): ValueSequence

}

//    def convert(value: Value): Value

abstract class AbstractValueType extends ValueType {

  def nullValue: Value = valueOf(null)

  def nullSequence: ValueSequence = sequenceOf(null)

  def toString(value: Value): String = {
    if (value == null) return null
    value.value match {
      case Some(v) => v.toString
      case None => null
    }
  }

  def valueOf(valueLoader: ValueLoader): Value = Value(this, valueLoader)

  def sequenceOf(values: Traversable[Value]): ValueSequence = new ValueSequence(this, values)

}

object TextType extends AbstractValueType {

  def name: String = "text"

  def valueOf(string: String): Value = Value(this, new StaticValueLoader(string))

  def valueOf(value: Any): Value = {
    if (value == null) nullValue else valueOf(value.toString)
  }

}

object BooleanType extends AbstractValueType {

  private val TRUE: Value = Value(this, new StaticValueLoader(true))
  private val FALSE: Value = Value(this, new StaticValueLoader(false))

  def name: String = "boolean"

  def valueOf(string: String): Value = {
    if (string == null) nullValue else valueOf(string.toBoolean)
  }

  def valueOf(value: Any): Value = {
    if (value == null) return nullValue
    value match {
      case b: Boolean => valueOf(b)
      case _ => valueOf(value.toString)
    }
  }

  private def valueOf(value: Boolean): Value = {
    if (value) TRUE else FALSE
  }
}

trait NumberType extends ValueType

object DecimalType extends AbstractValueType with NumberType {

  def name: String = "decimal"

  def valueOf(string: String): Value = valueOf(string.replaceAll(",", ".").trim.toDouble)

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

  def valueOf(string: String): Value = valueOf(string.replaceAll(",", ".").trim.toInt)

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

case class DateType(name: String = "date") extends AbstractValueType {

  override def valueOf(string: String): Value = ???

  override def valueOf(value: Any): Value = ???

}

//
//    case class DateTimeType(name: String = "datetime") extends ValueType
//
//    case class LocaleType(name: String = "locale") extends ValueType
//
//    case class BinaryType(name: String = "binary") extends ValueType
//
//    case class PointType(name: String = "point") extends ValueType
//
//    case class LineStringType(name: String = "linestring") extends ValueType
//
//    case class PolygonType(name: String = "polygon") extends ValueType
