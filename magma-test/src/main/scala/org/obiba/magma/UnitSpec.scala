package org.obiba.magma

import java.time.{ZoneId, Instant, Clock}

import com.typesafe.scalalogging.Logger
import org.scalatest._
import org.slf4j.LoggerFactory

abstract class UnitSpec extends FlatSpec
  with Matchers
  with OptionValues
  with Inside
  with Inspectors {

  val logger = Logger(LoggerFactory.getLogger(getClass))

  implicit val clock: Clock = Clock.fixed(Instant.now, ZoneId.systemDefault)

}
