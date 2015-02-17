package org.obiba.magma.staticds

import org.obiba.magma.entity._
import org.obiba.magma.value.TextType
import org.obiba.magma.{VariableValueSource, UnitSpec, VariableBean}

class StaticValueTableSpec extends UnitSpec {

  val ds = new StaticDatasource("ds")

  val emptyTable = StaticValueTable("table", ds, EntityType.Participant, Set())

  "An empty static ValueTable" should "have default values" in {
    emptyTable.name should be("table")
    emptyTable.datasource should be(ds)
    emptyTable.entityType should be(EntityType.Participant)
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

  "A static ValueTable" can "have variables" in {
    val ds = new StaticDatasource("test")
    val tableWriter = ds.createWriter("table", EntityType.Participant)
    val variableWriter = tableWriter.writeVariables
    variableWriter.writeVariable(VariableBean("variable1", EntityType.Participant, TextType))

    val table = ds.getTable("table").get
    table.hasVariable("variable1") should be(true)
    table.variableCount should be(1)
    table.variables should have size 1

    val variable = table.getVariable("variable1").get
    variable.name should be("variable1")
    variable.entityType should be(EntityType.Participant)
    variable.valueType should be(TextType)
    variable.index should be(0)

    table.getVariableValueSource("variable1") should be('defined)

    val valueSource: VariableValueSource = table.getVariableValueSource("variable1").get
    valueSource.variable should be(variable)
    valueSource.name should be("variable1")
  }

  it can "remove variable" in {
    val ds = new StaticDatasource("test")
    val tableWriter = ds.createWriter("table", EntityType.Participant)
    val variableWriter = tableWriter.writeVariables
    variableWriter.writeVariable(VariableBean("variable1", EntityType.Participant, TextType))

    val table = ds.getTable("table").get
    val variable = table.getVariable("variable1").get
    variableWriter.removeVariable(variable)

    table.hasVariable("variable1") should be(false)
    table.variableCount should be(0)
    table.variables should be(empty)
  }
}
