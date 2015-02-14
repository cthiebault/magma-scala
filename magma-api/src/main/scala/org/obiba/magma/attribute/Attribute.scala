package org.obiba.magma.attribute

import java.util.Locale

import org.obiba.magma.value.Value

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

trait ListAttributeAware extends AttributeAware {

  protected[attribute] var _attributes: List[Attribute] = List()

  override def attributes: List[Attribute] = _attributes

  def addAttribute(attribute: Attribute): Unit = {
    val index = indexOf(attribute)
    _attributes = if (index == -1) _attributes :+ attribute else _attributes.updated(index, attribute)
  }

  private def indexOf(attribute: Attribute): Int = {
    _attributes.indexWhere(a => a.name == attribute.name && a.namespace == attribute.namespace && a.locale == attribute.locale)
  }

}


trait AttributeWriter extends AttributeAware {

  def addAttribute(attribute: Attribute): Unit

  def removeAttributes(name: String): Unit

  def removeAttributes(name: String, namespace: String): Unit

  def removeAttribute(name: String, namespace: String, locale: Locale): Unit

  def clearAttributes(): Unit

}


trait ListAttributeWriter extends ListAttributeAware with AttributeWriter {

  override def removeAttributes(name: String): Unit = {
    _attributes = _attributes.filterNot {
      case Attribute(`name`, _, _, _) => true
      case _ => false
    }
  }

  override def removeAttributes(name: String, namespace: String): Unit = {
    _attributes = _attributes.filterNot {
      case Attribute(`name`, Some(`namespace`), _, _) => true
      case _ => false
    }
  }

  override def removeAttribute(name: String, namespace: String, locale: Locale): Unit = {
    _attributes = _attributes.filterNot {
      case Attribute(`name`, Some(`namespace`), Some(`locale`), _) => true
      case _ => false
    }
  }

  override def clearAttributes(): Unit = _attributes = List()

}