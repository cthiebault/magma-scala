package org.obiba.magma.datasource.staticds

import org.obiba.magma.{CategoryCode, CategoryBean, VariableBean, UnitSpec}
import org.obiba.magma.entity.EntityType
import org.obiba.magma.value.JavaObjToValueConverters.StringConverters
import org.obiba.magma.value.TextType

class VariableSpec extends UnitSpec {

  "A VariableBean" should "have a table reference" in {

    val ds = new StaticDatasource("test")
    ds.createWriter("table", EntityType.Participant)
    val table = ds.getTable("table").get

    val variable = VariableBean("var", EntityType.Participant, TextType)

    variable.getVariableReference(table) should be("test.table:var")
  }

}
