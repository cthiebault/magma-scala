package org.obiba.magma

import org.obiba.magma.attribute.AttributeWriter
import org.obiba.magma.value.{ValueType, Value}


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
  def entityType: String

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
  def occurrenceGroup: String

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
  def unit: String

  /**
   * The IANA mime-type of binary data if applicable.
   *
   * @return the IANA mime-type
   */
  def mimeType: String

  /**
   * Used when this variable value is a pointer to another {@code VariableEntity}. The value is considered to point to
   * the referenced entity's {@code identifier}.
   *
   * @return the { @code entityType} that this value points to, this method returns null when the variable doesn't point
   *         to another entity.
   */
  def referencedEntityType: String

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

  def getCategory(categoryName: String): Category

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