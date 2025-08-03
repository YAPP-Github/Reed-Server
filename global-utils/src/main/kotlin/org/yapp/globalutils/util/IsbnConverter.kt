package org.yapp.globalutils.util

object IsbnConverter {
    fun toIsbn13(isbn10: String?): String? {
        val sanitized = isbn10?.replace("-", "")?.uppercase()

        if (sanitized.isNullOrBlank() || sanitized.length != 10) {
            return null
        }
        if (!sanitized.matches(Regex("^[0-9]{9}[0-9X]$"))) {
            return null
        }

        val stem = "978" + sanitized.substring(0, 9)

        val sum = stem.mapIndexed { index, c ->
            val digit = c.digitToInt()
            if ((index + 1) % 2 == 0) digit * 3 else digit
        }.sum()

        val checkDigit = (10 - (sum % 10)) % 10

        return stem + checkDigit
    }
}
