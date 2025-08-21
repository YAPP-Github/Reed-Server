package org.yapp.globalutils.validator

import org.yapp.globalutils.util.RegexUtils

object BookDataValidator {
    private val PARENTHESIS_REGEX = Regex(RegexUtils.PARENTHESIS_PATTERN)

    fun removeParenthesesFromAuthor(author: String): String {
        return author.replace(PARENTHESIS_REGEX, "")
    }

    fun removeParenthesesFromPublisher(publisher: String): String {
        return publisher.replace(PARENTHESIS_REGEX, "")
    }
}
