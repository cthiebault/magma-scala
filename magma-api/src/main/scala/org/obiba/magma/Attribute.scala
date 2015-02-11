package org.obiba.magma

import java.util.Locale

case class Attribute(name: String, namespace: Option[String], locale: Option[Locale], value: Value)

object Attribute {
  def apply(name: String, namespace: String, locale: Locale, value: Value): Attribute = {
    new Attribute(name, Some(namespace), Some(locale), value)
  }

  def apply(name: String, namespace: String, value: Value): Attribute = {
    new Attribute(name, Some(namespace), None, value)
  }

  def apply(name: String, value: Value): Attribute = {
    new Attribute(name, None, None, value)
  }
}

trait AttributeAware {

  def attributes: List[Attribute]

  def hasAttributes: Boolean = attributes.nonEmpty

  def getAttributes(name: String = null, namespace: String): List[Attribute] = {
    attributes.filter(attr => attr.namespace.contains(namespace) && (name == null || name == attr.name))
  }

  def getAttribute(name: String, namespace: String, locale: Locale): Option[Attribute] = {
    getAttributes(name, namespace).find(attr => attr.locale.contains(locale))
  }

  def getAttributeValue(name: String, namespace: String, locale: Locale): Option[Value] = {
    getAttribute(name, namespace, locale) match {
      case Some(attr) => Some(attr.value)
      case None => None
    }
  }
}

trait AttributeWriter extends AttributeAware {

  def attributeValue(name: String, namespace: String = null, locale: Locale = null, value: Value): Unit

}