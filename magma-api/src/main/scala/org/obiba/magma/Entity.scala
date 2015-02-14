package org.obiba.magma

trait Entity {

  val `type`: String

  val identifier: String

}

case class EntityBean(`type`: String, identifier: String) extends Entity

trait EntityProvider {

  def entityType: String

  def isForEntityType(entityType: String): Boolean

  def entities: Set[Entity]

}

abstract class AbstractEntityProvider(val entityType: String) extends EntityProvider {

  override def isForEntityType(entityType: String): Boolean = entityType == this.entityType

}
