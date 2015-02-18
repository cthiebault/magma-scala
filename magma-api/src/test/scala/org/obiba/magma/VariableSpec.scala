package org.obiba.magma

import org.obiba.magma.entity.EntityType
import org.obiba.magma.staticds.StaticDatasource
import org.obiba.magma.value.TextType


class VariableSpec extends UnitSpec {

  "A VariableBean" should "have default values" in {
    val variable = VariableBean("var", EntityType.Participant, TextType)
    variable.name should be("var")
    variable.valueType should be(TextType)

    variable.entityType should be(EntityType.Participant)
    variable.isForEntityType("Participant") should be(right = true)

    variable.categories should be(empty)
    variable.hasCategories should be(false)

    variable.index should be(0)
    variable.isRepeatable should be(false)
    variable.mimeType should be('empty)
    variable.occurrenceGroup should be('empty)
    variable.referencedEntityType should be('empty)
    variable.unit should be('empty)
  }

  it should "have a category after adding one" in {
    val variable = VariableBean("var", EntityType.Participant, TextType)
    variable.addCategory(new CategoryBean("cat1", CategoryCode("1")))

    variable.hasCategories should be(true)
    variable.categories should have size (1)
    variable.getCategory("cat1") should be('defined)
    val category: Category = variable.getCategory("cat1").get
    category.name should be("cat1")
    category.code should be(CategoryCode("1"))
    category.missing should be(false)
    variable.areAllCategoriesMissing should be(false)
  }

  it should "support missing categories" in {
    val variable = VariableBean("var", EntityType.Participant, TextType)
    variable.addCategory(new CategoryBean("cat1", CategoryCode("1"), true))
    variable.addCategory(new CategoryBean("cat2", CategoryCode("2"), true))

    variable.hasCategories should be(true)
    variable.categories should have size (2)
    variable.areAllCategoriesMissing should be(true)
    variable.isMissingValue(TextType.valueOf("1")) should be(true)

    variable.addCategory(new CategoryBean("cat3", CategoryCode("3")))

    variable.categories should have size (3)
    variable.areAllCategoriesMissing should be(false)
    variable.isMissingValue(TextType.nullValue) should be(true)
    variable.isMissingValue(TextType.valueOf("cat3")) should be(false)
    variable.isMissingValue(TextType.valueOf("cat1")) should be(true)
    variable.isMissingValue(TextType.valueOf("cat2")) should be(true)
  }

  it should "have a table reference" in {

    val ds = new StaticDatasource("test")
    ds.createWriter("table", EntityType.Participant)
    val table = ds.getTable("table").get

    val variable = VariableBean("var", EntityType.Participant, TextType)

    variable.getVariableReference(table) should be("test.table:var")
  }

}
