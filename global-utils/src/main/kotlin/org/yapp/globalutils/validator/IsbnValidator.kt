package org.yapp.globalutils.validator

import org.yapp.globalutils.util.RegexUtils

object IsbnValidator {
    private val ISBN10_REGEX = Regex(RegexUtils.ISBN10_PATTERN)
    private val ISBN13_REGEX = Regex(RegexUtils.ISBN13_PATTERN)

    fun isValidIsbn10(isbnInput: String): Boolean {
        val isbn = preprocess(isbnInput)

        if (!isbn.matches(ISBN10_REGEX)) {
            return false
        }

        var sum = 0
        for (i in 0 until 9) {
            val digit = isbn[i].toString().toInt()
            sum += digit * (10 - i)
        }

        val lastChar = isbn[9]
        val checkDigit = if (lastChar == 'X') 10 else lastChar.toString().toInt()

        return (sum + checkDigit) % 11 == 0
    }

    fun isValidIsbn13(isbnInput: String): Boolean {
        val isbn = preprocess(isbnInput)

        if (!isbn.matches(ISBN13_REGEX)) {
            return false
        }

        var sum = 0
        for (i in 0 until 12) {
            val digit = isbn[i].toString().toInt()
            sum += if (i % 2 == 0) digit else digit * 3
        }

        val calculatedCheckDigit = (10 - (sum % 10)) % 10
        val actualCheckDigit = isbn[12].toString().toInt()

        return calculatedCheckDigit == actualCheckDigit
    }

    private fun preprocess(isbn: String): String {
        return isbn.replace("-", "")
            .replace(" ", "")
            .uppercase()
    }
}
