package org.obiba.magma

import java.util.Locale

import org.obiba.magma.ValueType

trait Attribute {

    def getNamespace: String

    def hasNamespace: Boolean

    def getName: String

    def getLocale: Locale

    def isLocalised: Boolean

    def getValueType: ValueType

    def getValue: Value

}
