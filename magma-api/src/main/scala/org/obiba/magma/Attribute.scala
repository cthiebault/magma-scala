package org.obiba.magma

import java.util.Locale

trait Attribute {

    def getName: String

    def getNamespace: Option[String]

    def getLocale: Option[Locale]

    def getValueType: ValueType

    def getValue: Value

}
