package org.yapp.globalutils.validator

object BookDataValidator {

    private val PARENTHESIS_REGEX = "\\s*\\([^)]*\\)\\s*".toRegex()

    fun removeParenthesesFromAuthor(author: String): String {
        return author.replace(PARENTHESIS_REGEX, "")
    }

    fun removeParenthesesFromPublisher(publisher: String): String {
        return publisher.replace(PARENTHESIS_REGEX, "")
    }
}
