package org.obiba.magma

import org.obiba.magma.attribute.{AttributeWriter, ListAttributeWriter}

trait Category extends AttributeWriter {

  def name: String

  def code: CategoryCode

  def missing: Boolean

}

case class CategoryCode(value: String) extends AnyVal

case class CategoryBean(name: String, code: CategoryCode, missing: Boolean = false)
  extends Category with ListAttributeWriter

