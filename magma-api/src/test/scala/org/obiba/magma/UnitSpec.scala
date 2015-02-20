package org.obiba.magma

import org.obiba.magma.logging.Slf4jLogging
import org.scalatest._

abstract class UnitSpec extends FlatSpec
  with Matchers
  with OptionValues
  with Inside
  with Inspectors
  with Slf4jLogging
