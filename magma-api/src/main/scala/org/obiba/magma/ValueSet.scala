package org.obiba.magma

trait ValueSet extends Timestamped {

  def valueTable: ValueTable

  def entity: Entity

}
