package org.obiba.magma

trait ValueLoader {

  /**
   * Load the value from its reference.
   */
  def getValue: Option[Any]

  /**
   * Get the size of the value.
   */
  def getLength: Long

}

object ValueLoader {

  class StaticValueLoader(value: Any) extends ValueLoader {

    val option: Option[Any] = if (value == null) None else Some(value)

    def getLength: Long = {
      option match {
        case Some(_) =>
          throw new UnsupportedOperationException
        case None =>
          0
      }
    }

    def getValue: Option[Any] = option
  }

}


