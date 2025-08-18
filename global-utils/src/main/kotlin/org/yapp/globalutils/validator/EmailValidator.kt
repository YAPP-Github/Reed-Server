package org.yapp.globalutils.validator

import org.yapp.globalutils.util.RegexUtils

object EmailValidator {
    private val EMAIL_REGEX = Regex(RegexUtils.EMAIL_PATTERN)

    fun isValidEmail(email: String): Boolean {
        return email.matches(EMAIL_REGEX)
    }
}
