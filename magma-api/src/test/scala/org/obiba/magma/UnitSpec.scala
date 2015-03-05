package org.obiba.magma

import java.time.Clock

import org.obiba.magma.logging.Slf4jLogging
import org.scalatest._

abstract class UnitSpec extends FlatSpec
  with Matchers
  with OptionValues
  with Inside
  with Inspectors
  with Slf4jLogging {

  //TODO replace by FixedClock
  implicit val clock: Clock = Clock.systemUTC

}
