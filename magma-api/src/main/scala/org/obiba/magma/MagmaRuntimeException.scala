package org.obiba.magma

/**
 * Base class for all Magma runtime exceptions. Any exception thrown by Magma runtime should extend this class.
 */
class MagmaRuntimeException(message: String = null, cause: Throwable = null)
  extends RuntimeException(message, cause)