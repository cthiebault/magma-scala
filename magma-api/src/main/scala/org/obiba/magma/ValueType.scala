package org.obiba.magma

sealed trait ValueType {

    val name: String

    def getName: String = name

    case class TextType(name: String = "text") extends ValueType

    case class BooleanType(name: String = "boolean") extends ValueType

    case class DateType(name: String = "date") extends ValueType

    case class DateTimeType(name: String = "datetime") extends ValueType

    case class LocaleType(name: String = "locale") extends ValueType

    case class DecimalType(name: String = "decimal") extends ValueType

    case class IntegerType(name: String = "integer") extends ValueType

    case class BinaryType(name: String = "binary") extends ValueType

    case class PointType(name: String = "point") extends ValueType

    case class LineStringType(name: String = "linestring") extends ValueType

    case class PolygonType(name: String = "polygon") extends ValueType

}
