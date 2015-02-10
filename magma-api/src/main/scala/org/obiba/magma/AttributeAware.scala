package org.obiba.magma

import java.util.Locale

trait AttributeAware {

    def hasAttributes: Boolean

    def getAttribute(name: String, namespace: String = null, locale: Locale = null): Option[Attribute]

    def getAttributeValue(name: String, namespace: String = null, locale: Locale = null): Option[Value]

    def getAttributes(name: String, namespace: String = null): List[Attribute]

    def getNamespaceAttributes(namespace: String): List[Attribute]

    def getAttributes: List[Attribute]
}
