package org.yapp.globalutils.util

object RegexUtils {
    const val ISBN10_PATTERN = "^[0-9]{9}[0-9X]$"
    const val ISBN13_PATTERN = "^(978|979)\\d{10}$"
    const val AUTHOR_MARKER_PATTERN = " (?:\\(지은이\\)|\\(글\\))"
    const val PARENTHESIS_PATTERN = "\\s*\\([^)]*\\)\\s*"
    const val EMAIL_PATTERN="^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"
}
