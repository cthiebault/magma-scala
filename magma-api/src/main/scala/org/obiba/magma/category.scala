package org.obiba.magma

import org.obiba.magma.attribute.{AttributeWriter, ListAttributeWriter}

trait Category extends AttributeWriter {

  def name: String

  def code: String

  def missing: Boolean

}

case class CategoryBean(name: String, code: String, missing: Boolean) 
    extends Category with ListAttributeWriter