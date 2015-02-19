package org.obiba.magma.logging

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

trait Slf4jLogging {

  val logger = Logger(LoggerFactory.getLogger(getClass))

}
