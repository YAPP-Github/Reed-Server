package org.yapp.apis.util

object AuthorExtractor {
    private const val AUTHOR_MARKER = "(지은이)"
    private const val CLOSING_PAREN = ")"
    private const val DELIMITER = "), "

    fun extractAuthors(authorString: String?): String {
        if (authorString.isNullOrBlank() || !authorString.contains(AUTHOR_MARKER)) {
            return ""
        }

        var authorsPart = authorString.substringBefore(" $AUTHOR_MARKER")

        if (authorsPart.contains(CLOSING_PAREN)) {
            authorsPart = authorsPart.substringAfterLast(DELIMITER)
        }

        return authorsPart.trim()
    }
}
