package org.yapp.globalutils.validator

object EmailValidator {
    private val EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$".toRegex()

    fun isValidEmail(email: String): Boolean {
        return email.matches(EMAIL_REGEX)
    }
}
