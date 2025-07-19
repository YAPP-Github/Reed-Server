package org.yapp.globalutils.validator

object IsbnValidator {
    private val ISBN_REGEX = Regex("^(.{10}|.{13})$")

    fun isValidIsbn(isbn: String): Boolean {
        return isbn.matches(ISBN_REGEX)
    }
}
