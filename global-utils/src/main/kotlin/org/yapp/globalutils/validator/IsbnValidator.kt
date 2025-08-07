package org.yapp.globalutils.validator

import org.yapp.globalutils.util.RegexUtils

object IsbnValidator {
    private val ISBN10_REGEX = Regex(RegexUtils.ISBN10_PATTERN)
    private val ISBN13_REGEX = Regex(RegexUtils.ISBN13_PATTERN)

    fun isValidIsbn10(isbn: String): Boolean {
        return isbn.matches(ISBN10_REGEX)
    }

    fun isValidIsbn13(isbn: String): Boolean {
        return isbn.matches(ISBN13_REGEX)
    }
}
