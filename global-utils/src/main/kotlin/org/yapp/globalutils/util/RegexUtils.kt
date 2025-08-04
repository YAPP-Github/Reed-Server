package org.yapp.globalutils.util

object RegexUtils {
    const val ISBN10_PATTERN = "^[0-9]{9}[0-9X]$"
    const val ISBN13_PATTERN = "^(978|979)\\d{10}$"
}
