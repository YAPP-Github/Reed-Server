package org.yapp.globalutils.util


object RegexUtils {

    val EMAIL_PATTERN = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    val PROFILE_IMAGE_URL_PATTERN = Regex("^https?://[a-zA-Z0-9.-]+(/.*)?$")


    fun isValidEmail(email: String): Boolean {
        return email.matches(EMAIL_PATTERN)
    }

    fun isValidProfileImageUrl(url: String): Boolean {
        return url.matches(PROFILE_IMAGE_URL_PATTERN)
    }
}
