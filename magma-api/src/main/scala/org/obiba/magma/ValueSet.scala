package org.obiba.magma

trait ValueSet extends Timestamped {

  def valueTable: ValueTable

  def entity: Entity

}

case class ValueSetBean(valueTable: ValueTable, entity: Entity) extends ValueSet {
  override def timestamps: Timestamps = valueTable.timestamps
}