package org.obiba.magma

import org.obiba.magma.attribute.{AttributeWriter, ListAttributeWriter}
import org.obiba.magma.entity.EntityType
import org.obiba.magma.value.{Value, ValueType}

/**
 * The meta-data of a {@code Value}. {@code Value} instances can be obtained for {@code ValueSet} instances. When this
 * value requires description, this is done through an instance of {@code Variable}.
 */
trait Variable extends AttributeWriter {

  /**
   * The name of the variable. A variable's name must be unique within its {@code Collection}.
   *
   * @return the name of the variable.
   */
  def name: String

  /**
   * Returns the {@code entityType} this variable is associated with.
   *
   * @return
   */
  def entityType: EntityType

  /**
   * Returns true when this variable is for values of the specified {@code entityType}
   *
   * @param type the type of entity to test
   * @return true when this variable is for values of the specified { @code entityType}, false otherwise.
   */
  def isForEntityType(`type`: String): Boolean

  /**
   * Returns true when this variable is repeatable. A repeatable variable is one where multiple {@code Value} instances
   * may exist within a single {@code ValueSet}. A single {@code Value} within a {@code ValueSet} is referenced by an
   * {@link Occurrence} instance.
   *
   * @return true when this variable may have multiple values within a single { @code ValueSet}
   */
  def isRepeatable: Boolean

  /**
   * When a variable is repeatable, the repeated values are grouped together, this method returns the name of this
   * group. The name is arbitrary but must be unique within a {@code Collection}.
   *
   * @return the name of the repeating group
   */
  def occurrenceGroup: Option[String]

  /**
   * Returns the {@code ValueType} of this variable instance. Any {@code Value} obtained for this {@code variable}
   * should be of this type.
   *
   * @return the { @code ValueType} of this variable.
   */
  def valueType: ValueType

  /**
   * The SI code of the measurement unit if applicable.
   *
   * @return unit
   */
  def unit: Option[String]

  /**
   * The IANA mime-type of binary data if applicable.
   *
   * @return the IANA mime-type
   */
  def mimeType: Option[String]

  /**
   * Used when this variable value is a pointer to another {@code Entity}. The value is considered to point to
   * the referenced entity's {@code identifier}.
   *
   * @return the { @code entityType} that this value points to, this method returns null when the variable doesn't point
   *         to another entity.
   */
  def referencedEntityType: Option[EntityType]

  /**
   * Get the index (or position) of this variable in the list of variables of a table. Default value is 0, in which case
   * the natural order should apply.
   *
   * @return
   */
  def index: Int

  /**
   * Returns true if this variable has at least on {@code Category}
   *
   * @return
   */
  def hasCategories: Boolean

  /**
   * Returns the set of categories for this {@code Variable}. This method returns null when the variable has no
   * categories. To determine if a {@code Variable} instance has categories, use the {@code #hasCategories()} method.
   *
   * @return a { @code Set} of { @code Category} instances or null if none exist
   */
  def categories: Set[Category]

  def getCategory(name: String): Option[Category]

  def addCategory(category: Category)

  /**
   * Returns true when {@code value} is equal to a {@code Category} marked as {@code missing} or when
   * {@code Value#isNull} returns true
   *
   * @param value the value to test
   * @return true when the value is considered { @code missing}, false otherwise.
   */
  def isMissingValue(value: Value): Boolean

  def areAllCategoriesMissing: Boolean

  def getVariableReference(table: ValueTable): String

}

object VariableReference {
  def getReference(datasource: String, table: String): String = {
    s"$datasource.$table"
  }

  def getReference(table: ValueTable, variable: Variable): String = {
    table.tableReference + ":" + variable.name
  }

  def getReference(datasource: String, table: String, variable: String): String = {
    ValueTableReference.getReference(datasource, table) + ":" + variable
  }
}

case class VariableBean private (
  name: String,
  entityType: EntityType,
  valueType: ValueType,
  index: Int,
  isRepeatable: Boolean = false,
  unit: Option[String] = None,
  mimeType: Option[String] = None,
  occurrenceGroup: Option[String] = None,
  referencedEntityType: Option[EntityType] = None) extends Variable with ListAttributeWriter {

  private var _categories: Set[Category] = Set()

  override def isForEntityType(`type`: String): Boolean = entityType == EntityType(`type`)

  override def isMissingValue(value: Value): Boolean = {
    if (value.isNull || !hasCategories) {
      return value.isNull
    }
    categories.map(c => valueType.valueOf(c.name) -> c).toMap.get(value) match {
      case Some(c) => c.missing
      case _ => true
    }
  }

  override def hasCategories: Boolean = categories.nonEmpty

  override def getCategory(name: String): Option[Category] = categories.find(_.name == name)

  override def categories: Set[Category] = _categories

  override def areAllCategoriesMissing: Boolean = categories.filterNot(_.missing).isEmpty

  override def getVariableReference(table: ValueTable): String = VariableReference.getReference(table, this)

  override def addCategory(category: Category): Unit = _categories = _categories + category
}

object VariableBean {

  def apply(name: String, entityType: EntityType, valueType: ValueType): VariableBean = {
    new VariableBean(name, entityType, valueType, 0)
  }

}
