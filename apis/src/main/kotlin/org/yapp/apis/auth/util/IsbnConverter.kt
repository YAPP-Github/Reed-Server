package org.yapp.apis.util

import org.yapp.globalutils.util.RegexUtils

object IsbnConverter {
    private const val ISBN10_LENGTH = 10
    private const val ISBN13_PREFIX = "978"
    private const val ISBN13_MODULUS = 10
    private const val WEIGHT_EVEN = 3
    private const val WEIGHT_ODD = 1

    fun toIsbn13(isbn10: String?): String? {
        val sanitized = isbn10?.replace("-", "")?.uppercase()

        if (sanitized.isNullOrBlank() || sanitized.length != ISBN10_LENGTH) {
            return null
        }
        if (!sanitized.matches(Regex(RegexUtils.ISBN10_PATTERN))) {
            return null
        }

        val stem = ISBN13_PREFIX + sanitized.substring(0, ISBN10_LENGTH - 1)

        val sum = stem.mapIndexed { index, c ->
            val digit = c.digitToInt()
            if ((index + 1) % 2 == 0) digit * WEIGHT_EVEN else digit * WEIGHT_ODD
        }.sum()

        val checkDigit = (ISBN13_MODULUS - (sum % ISBN13_MODULUS)) % ISBN13_MODULUS

        return stem + checkDigit
    }
}
