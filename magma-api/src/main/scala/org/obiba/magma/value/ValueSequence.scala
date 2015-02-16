package org.obiba.magma.value

class ValueSequence(valueType: ValueType, valueLoader: ValueLoader) extends Value(valueType, valueLoader) {

  def this(valueType: ValueType, value: Traversable[Value]) = this(valueType, new ValueLoader.StaticValueLoader(value))

  override def value: Option[List[Value]] = {
    super.value.asInstanceOf[Option[List[Value]]]
  }

  def values: List[Value] = value.getOrElse(List())

  def size: Int = {
    value match {
      case Some(list) => list.size
      case None => 0
    }
  }

  override def length: Long = {
    value
      .getOrElse(List())
      .foldLeft(0l) { (total, value) => total + value.length}
  }
}

object ValueSequence {

  def apply(valueType: ValueType, values: Traversable[Value]): Value = new ValueSequence(valueType, values)

  def apply(valueType: ValueType): Value = apply(valueType, null)

}
