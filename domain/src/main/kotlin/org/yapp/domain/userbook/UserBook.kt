package org.yapp.domain.userbook

import org.yapp.globalutils.util.UuidGenerator
import org.yapp.globalutils.validator.IsbnValidator
import java.time.LocalDateTime
import java.util.*

data class UserBook private constructor(
    val id: Id,
    val userId: UserId,
    val bookId: BookId,
    val bookIsbn13: BookIsbn13,
    val coverImageUrl: String,
    val publisher: String,
    val title: String,
    val author: String,
    val status: BookStatus,
    val readingRecordCount: Int = 0,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
) {
    fun updateStatus(newStatus: BookStatus): UserBook {
        return this.copy(status = newStatus)
    }

    fun increaseReadingRecordCount(): UserBook {
        return this.copy(readingRecordCount = this.readingRecordCount + 1)
    }

    companion object {
        fun create(
            userId: UUID,
            bookId: UUID,
            bookIsbn13: String,
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
                bookIsbn13 = BookIsbn13.newInstance(bookIsbn13),
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
            bookIsbn13: BookIsbn13,
            coverImageUrl: String,
            publisher: String,
            title: String,
            author: String,
            status: BookStatus,
            readingRecordCount: Int,
            createdAt: LocalDateTime? = null,
            updatedAt: LocalDateTime? = null,
            deletedAt: LocalDateTime? = null
        ): UserBook {
            return UserBook(
                id = id,
                userId = userId,
                bookId = bookId,
                bookIsbn13 = bookIsbn13,
                coverImageUrl = coverImageUrl,
                publisher = publisher,
                title = title,
                author = author,
                status = status,
                readingRecordCount = readingRecordCount,
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
    value class BookIsbn13(val value: String) {
        companion object {
            fun newInstance(value: String): BookIsbn13 {
                require(IsbnValidator.isValidIsbn13(value)) { "ISBN13 must be a 13-digit number." }
                return BookIsbn13(value)
            }
        }
    }
}
