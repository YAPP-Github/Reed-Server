package org.yapp.globalutils.util


object RegexUtils {

    val EMAIL_PATTERN = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

    fun isValidEmail(email: String): Boolean {
        return email.matches(EMAIL_PATTERN)
    }
}
