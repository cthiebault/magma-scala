package org.obiba.magma.staticds

import org.obiba.magma.UnitSpec
import org.obiba.magma.entity._
import org.obiba.magma.static.StaticDatasource

class StaticValueTableSpec extends UnitSpec {

  val ds = new StaticDatasource("ds")

  val emptyTable = StaticValueTable("table", ds, EntityType.Participant, Set())

  "An empty static ValueTable" should "have default values" in {
    emptyTable.name should be("table")
    emptyTable.datasource should be(ds)
    emptyTable.entityType should be("Participant")
    emptyTable.canDrop should be(true)
    emptyTable.canDropValueSets should be(false)
    emptyTable.isView should be(false)
  }
  it should "have no variable" in {
    emptyTable.variables should be(empty)
    emptyTable.variableCount should be(0)
    emptyTable.hasVariable("none") should be(false)
  }
  it should "have no value sets" in {
    emptyTable.valueSets should be(empty)
    emptyTable.valueSetCount should be(0)
    emptyTable.hasValueSet(ParticipantEntityBean("1")) should be(false)
  }
  it should "have no entities" in {
    emptyTable.entities should be(empty)
    emptyTable.entityCount should be(0)
    emptyTable.isForEntityType("Participant") should be(true)
  }

  //  "A static ValueTable" should "have default values" in {
  //
  //    val table = StaticValueTable("table", ds, "Participant", Set())
  //    table.
  //  }
}
