package org.yapp.domain.book

import org.yapp.globalutils.util.UuidGenerator
import org.yapp.globalutils.validator.IsbnValidator
import java.time.LocalDateTime
import java.util.UUID

/**
 * Represents a book in the domain model.
 */
data class Book private constructor(
    val id: Id,
    val isbn: Isbn,
    val title: String,
    val author: String,
    val publisher: String,
    val publicationYear: Int?,
    val coverImageUrl: String,
    val description: String?,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
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
            description: String? = null,
        ): Book {
            return Book(
                id = Id.newInstance(UuidGenerator.create()),
                isbn = Isbn.newInstance(isbn),
                title = title,
                author = author,
                publisher = publisher,
                publicationYear = publicationYear,
                coverImageUrl = coverImageUrl,
                description = description,
            )
        }

        fun reconstruct(
            id: Id,
            isbn: Isbn,
            title: String,
            author: String,
            publisher: String,
            publicationYear: Int?,
            coverImageUrl: String,
            description: String?,
            createdAt: LocalDateTime? = null,
            updatedAt: LocalDateTime? = null,
            deletedAt: LocalDateTime? = null
        ): Book {
            return Book(
                id = id,
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
    value class Id(val value: UUID) {
        companion object {
            fun newInstance(value: UUID) = Id(value)
        }
    }

    @JvmInline
    value class Isbn(val value: String) {
        companion object {
            fun newInstance(value: String): Isbn {
                require(IsbnValidator.isValidIsbn(value)) { "ISBN must be a 10 or 13-digit number." }
                return Isbn(value)
            }
        }
    }
}
