package org.obiba.magma.utils

import com.google.common.base.Strings

object StringUtils {

  implicit class StringsWrapper(val str: String) extends AnyVal {
    def isNullOrEmpty: Boolean = Strings.isNullOrEmpty(str)
  }

}
