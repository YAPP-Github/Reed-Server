package org.yapp.domain.book

import java.time.LocalDateTime // Import LocalDateTime

/**
 * Represents a book in the domain model.
 */
data class Book private constructor(
    val isbn: Isbn,
    val title: String,
    val author: String,
    val publisher: String,
    val publicationYear: Int?,
    val coverImageUrl: String,
    val description: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime? = null
) {


    companion object {
        fun create(
            isbn: String,
            title: String,
            author: String,
            publisher: String,
            coverImageUrl: String,
            publicationYear: Int? = null,
            description: String? = null
        ): Book {
            val now = LocalDateTime.now()
            return Book(
                isbn = Isbn.newInstance(isbn),
                title = title,
                author = author,
                publisher = publisher,
                publicationYear = publicationYear,
                coverImageUrl = coverImageUrl,
                description = description,
                createdAt = now,
                updatedAt = now,
                deletedAt = null
            )
        }

        fun reconstruct(
            isbn: Isbn,
            title: String,
            author: String,
            publisher: String,
            publicationYear: Int?,
            coverImageUrl: String,
            description: String?,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime,
            deletedAt: LocalDateTime? = null
        ): Book {
            return Book(
                isbn = isbn,
                title = title,
                author = author,
                publisher = publisher,
                publicationYear = publicationYear,
                coverImageUrl = coverImageUrl,
                description = description,
                createdAt = createdAt,
                updatedAt = updatedAt,
                deletedAt = deletedAt
            )
        }
    }

    @JvmInline
    value class Isbn(val value: String) {
        companion object {
            fun newInstance(value: String): Isbn {
                require(value.matches(Regex("^(\\d{10}|\\d{13})$"))) { "ISBN은 10자리 또는 13자리 숫자여야 합니다." }
                return Isbn(value)
            }
        }
    }
}
