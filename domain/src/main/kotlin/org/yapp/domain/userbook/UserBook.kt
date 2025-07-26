package org.yapp.domain.userbook

import org.yapp.globalutils.util.UuidGenerator
import org.yapp.globalutils.validator.IsbnValidator
import java.time.LocalDateTime
import java.util.*

data class UserBook private constructor(
    val id: Id,
    val userId: UserId,
    val bookId: BookId,
    val bookIsbn: BookIsbn,
    val coverImageUrl: String,
    val publisher: String,
    val title: String,
    val author: String,
    val status: BookStatus,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
) {
    fun updateStatus(newStatus: BookStatus): UserBook {
        return this.copy(status = newStatus)
    }

    companion object {
        fun create(
            userId: UUID,
            bookId: UUID,
            bookIsbn: String,
            coverImageUrl: String,
            publisher: String,
            title: String,
            author: String,
            status: BookStatus
        ): UserBook {
            return UserBook(
                id = Id.newInstance(UuidGenerator.create()),
                userId = UserId.newInstance(userId),
                bookId = BookId.newInstance(bookId),
                bookIsbn = BookIsbn.newInstance(bookIsbn),
                coverImageUrl = coverImageUrl,
                publisher = publisher,
                title = title,
                author = author,
                status = status,
            )
        }

        fun reconstruct(
            id: Id,
            userId: UserId,
            bookId: BookId,
            bookIsbn: BookIsbn,
            coverImageUrl: String,
            publisher: String,
            title: String,
            author: String,
            status: BookStatus,
            createdAt: LocalDateTime? = null,
            updatedAt: LocalDateTime? = null,
            deletedAt: LocalDateTime? = null
        ): UserBook {
            return UserBook(
                id = id,
                userId = userId,
                bookId = bookId,
                bookIsbn = bookIsbn,
                coverImageUrl = coverImageUrl,
                publisher = publisher,
                title = title,
                author = author,
                status = status,
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
    value class UserId(val value: UUID) {
        companion object {
            fun newInstance(value: UUID) = UserId(value)
        }
    }

    @JvmInline
    value class BookId(val value: UUID) {
        companion object {
            fun newInstance(value: UUID) = BookId(value)
        }
    }

    @JvmInline
    value class BookIsbn(val value: String) {
        companion object {
            fun newInstance(value: String): BookIsbn {
                require(IsbnValidator.isValidIsbn(value)) { "ISBN must be a 10 or 13-digit number." }
                return BookIsbn(value)
            }
        }
    }
}
