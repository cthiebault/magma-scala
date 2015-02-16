package org.obiba.magma.value

class Value(val valueType: ValueType, private val valueLoader: ValueLoader) {

  def isNull: Boolean = value.isEmpty

  def length: Long = {
    try {
      if (isNull) 0 else valueLoader.getLength
    }
    catch {
      case e: UnsupportedOperationException => {
        // fallback to the length of the string representation of the value
        val str: String = toString
        if (str == null) 0 else str.length
      }
    }
  }

  def value: Option[Any] = {
    if (valueLoader == null) None else valueLoader.getValue
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Value]

  override def equals(other: Any): Boolean = other match {
    case that: Value =>
      (that canEqual this) &&
          valueType == that.valueType &&
          valueLoader.getValue == that.valueLoader.getValue
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(valueType, valueLoader)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def toString = valueType.toString(this)
}

object Value {

  def apply(valueType: ValueType, valueLoader: ValueLoader): Value = new Value(valueType, valueLoader = valueLoader)

  def apply(valueType: ValueType, value: Any): Value = new Value(valueType, new ValueLoader.StaticValueLoader(value))

  def apply(valueType: ValueType): Value = apply(valueType, null.asInstanceOf[Any])

}



