package org.obiba.magma.entity

trait Entity {

  val `type`: EntityType

  val identifier: EntityIdentifier

}

case class EntityType(name: String) extends AnyVal

object EntityType {
  val Participant = EntityType("Participant")
}

case class EntityIdentifier(value: String) extends AnyVal

case class EntityBean(`type`: EntityType, identifier: EntityIdentifier) extends Entity

object ParticipantEntityBean {
  def apply(identifier: String): EntityBean = EntityBean(EntityType.Participant, EntityIdentifier(identifier))
}

trait EntityProvider {

  def `type`: EntityType

  def isForEntityType(`type`: EntityType): Boolean

  def entities: Set[Entity]

}

abstract class AbstractEntityProvider(val `type`: EntityType) extends EntityProvider {

  override def isForEntityType(`type`: EntityType): Boolean = `type` == this.`type`

}
