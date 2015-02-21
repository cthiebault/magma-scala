package org.obiba.magma.entity

import org.obiba.magma.UnitSpec

class EntitySpec extends UnitSpec {

  "An EntityBean" should "be sortable" in {

    val list: List[Entity] = List(
      EntityBean(EntityType("A"), EntityIdentifier("2")),
      EntityBean(EntityType("A"), EntityIdentifier("1")),
      EntityBean(EntityType("B"), EntityIdentifier("1")),
      EntityBean(EntityType("B"), EntityIdentifier("2"))
    )

    list
      .sorted
      .map(_.toString)
      .mkString(",") should be("entity[A:1],entity[A:2],entity[B:1],entity[B:2]")
  }
  
}
