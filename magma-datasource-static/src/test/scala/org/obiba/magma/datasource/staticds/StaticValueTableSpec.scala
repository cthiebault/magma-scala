package org.obiba.magma.datasource.staticds

import java.time.Instant

import org.obiba.magma._
import org.obiba.magma.entity._
import org.obiba.magma.value.TextType
import org.obiba.magma.value.JavaObjToValueConverters.StringConverters

import scala.collection.immutable.TreeSet

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
    emptyTable.getVariable("none") should be('empty)
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
    table.timestamps.created should not be null
    table.timestamps.lastUpdate should be('empty)

    val variable = table.getVariable("variable1").get
    variable.name should be("variable1")
    variable.entityType should be(EntityType.Participant)
    variable.valueType should be(TextType)
    variable.index should be(0)

    table.getVariableValueSource("variable1") should be('defined)

    val valueSource: VariableValueSource = table.getVariableValueSource("variable1").get
    valueSource.variable should be(variable)
    valueSource.name should be("variable1")
    valueSource.valueType should be(TextType)
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

  it can "have valueSets" in {
    val ds = new StaticDatasource("test")
    val tableWriter = ds.createWriter("table", EntityType.Participant)
    val table = ds.getTable("table").get
    val variableWriter = tableWriter.writeVariables
    variableWriter.writeVariable(VariableBean("variable1", EntityType.Participant, TextType))
    val variable = table.getVariable("variable1").get

    val valueSetWriter = tableWriter.writeValueSet(ParticipantEntityBean("1"))
    valueSetWriter.writeValue(variable, "value 1".toTextValue)

    table.entityCount should be(1)
    table.entities should have size 1
    table.hasEntity(ParticipantEntityBean("1")) should be(true)
    table.hasValueSet(ParticipantEntityBean("1")) should be(true)
    table.timestamps.lastUpdate should not be null

    table.getValueSet(ParticipantEntityBean("1")) should be('defined)
    table.getValueSet(ParticipantEntityBean("unknown")) should be('empty)
    val valueSet: ValueSet = table.getValueSet(ParticipantEntityBean("1")).get
    valueSet.entity should be(ParticipantEntityBean("1"))
    valueSet.valueTable should be(table)
    valueSet.timestamps should not be null

    table.getValueSetTimestamps(ParticipantEntityBean("1")).get should not be null
    table.getValueSetTimestamps(ParticipantEntityBean("unknown")) should be('empty)

    table.getValueSetTimestamps(TreeSet[Entity](ParticipantEntityBean("1"))) should have size 1

    table.getValue(variable, valueSet) should be('defined)
    table.getValue(VariableBean("variable2", EntityType.Participant, TextType), valueSet) should be('empty)
    val value = table.getValue(variable, valueSet).get
    value.isNull should be(false)
    value should be("value 1".toTextValue)

    val valueSource: VariableValueSource = table.getVariableValueSource("variable1").get
    valueSource.getValue(valueSet) should be("value 1".toTextValue)
    valueSource.supportVectorSource should be(true)
  }

  it can "remove valueSets" in {

    val ds = new StaticDatasource("test")
    val tableWriter = ds.createWriter("table", EntityType.Participant)
    val table = ds.getTable("table").get
    tableWriter.writeVariables.writeVariable(VariableBean("variable1", EntityType.Participant, TextType))
    val variable = table.getVariable("variable1").get

    val valueSetWriter = tableWriter.writeValueSet(ParticipantEntityBean("1"))
    valueSetWriter.writeValue(variable, "value 1".toTextValue)
    val lastUpdate: Option[Instant] = table.timestamps.lastUpdate
    lastUpdate should not be('empty)

    Thread sleep 1000

    valueSetWriter.remove()

    table.entityCount should be(0)
    table.entities should be(empty)
    table.hasEntity(ParticipantEntityBean("1")) should be(false)
    table.hasValueSet(ParticipantEntityBean("1")) should be(false)
    table.timestamps.lastUpdate.get should be > lastUpdate.get
  }

  it can "have vectorSource" in {
    val ds = new StaticDatasource("test")
    val tableWriter = ds.createWriter("table", EntityType.Participant)
    val table = ds.getTable("table").get
    val variableWriter = tableWriter.writeVariables
    variableWriter.writeVariable(VariableBean("variable1", EntityType.Participant, TextType))
    val variable = table.getVariable("variable1").get

    val valueSetWriter = tableWriter.writeValueSet(ParticipantEntityBean("1"))
    valueSetWriter.writeValue(variable, "value 1".toTextValue)
    valueSetWriter.writeValue(variable, "value 2".toTextValue)

    val vectorSource: VectorSource = table.getVariableValueSource("variable1").get.asVectorSource
    vectorSource should not be null
    vectorSource.valueType should be(TextType)
    vectorSource.getValues(TreeSet[Entity](ParticipantEntityBean("1"))) should have size 1
  }

}
