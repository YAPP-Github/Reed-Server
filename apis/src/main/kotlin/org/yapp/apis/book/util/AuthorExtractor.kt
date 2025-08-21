package org.yapp.apis.book.util

import org.yapp.globalutils.util.RegexUtils

object AuthorExtractor {
    private val AUTHOR_MARKER_REGEX = Regex(RegexUtils.AUTHOR_MARKER_PATTERN)
    private const val CLOSING_PAREN = ")"
    private const val DELIMITER = "), "
    private const val EXPECTED_PARTS_ON_SPLIT = 2
    private const val AUTHOR_PART_INDEX = 0

    fun extractAuthors(authorString: String?): String {
        if (authorString.isNullOrBlank()) {
            return ""
        }

        val partBeforeMarker = getPartBeforeMarker(authorString) ?: return ""
        val finalAuthor = cleanUpContributors(partBeforeMarker)

        return finalAuthor.trim()
    }

    private fun getPartBeforeMarker(text: String): String? {
        val parts = text.split(AUTHOR_MARKER_REGEX, limit = EXPECTED_PARTS_ON_SPLIT)

        return if (parts.size == EXPECTED_PARTS_ON_SPLIT) {
            parts[AUTHOR_PART_INDEX]
        } else {
            null
        }
    }

    private fun cleanUpContributors(authorPart: String): String {
        return if (authorPart.contains(CLOSING_PAREN)) {
            authorPart.substringAfterLast(DELIMITER, missingDelimiterValue = authorPart)
        } else {
            authorPart
        }
    }
}
