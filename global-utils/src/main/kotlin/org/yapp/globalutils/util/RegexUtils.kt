package org.yapp.globalutils.util

object RegexUtils {

    val EMAIL_PATTERN = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    val PROFILE_IMAGE_URL_PATTERN = Regex("^https?://[a-zA-Z0-9.-]+(/.*)?$")

    const val NOT_BLANK_AND_NOT_NULL_STRING_PATTERN = "^(?!null$|NULL$|\\s*$).+" // Removed the old ISBN pattern

    fun isValidEmail(email: String): Boolean {
        return email.matches(EMAIL_PATTERN)
    }

    fun isValidProfileImageUrl(url: String): Boolean {
        return url.matches(PROFILE_IMAGE_URL_PATTERN)
    }
}
