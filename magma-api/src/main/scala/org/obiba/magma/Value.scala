package org.obiba.magma

class Value(val valueType: ValueType, private val valueLoader: ValueLoader) {

    def isNull: Boolean = getValue.isEmpty

    def getLength: Long = {
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

    def getValue: Option[Any] = {
        valueLoader.getValue
    }

}

object Value {

    def apply(valueType: ValueType, value: Serializable): Value = {
        new Value(valueType, new ValueLoader.StaticValueLoader(value))
    }

    def apply(valueType: ValueType, valueLoader: ValueLoader): Value = {
        new Value(valueType, valueLoader)
    }
}

class ValueSequence(override val valueType: ValueType, private val valueLoader: ValueLoader)
    extends Value(valueType, valueLoader) {

}
