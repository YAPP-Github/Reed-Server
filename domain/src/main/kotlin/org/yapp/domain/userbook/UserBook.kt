package org.yapp.domain.userbook

import org.yapp.domain.book.Book
import org.yapp.domain.user.User
import org.yapp.globalutils.util.UuidGenerator
import java.time.LocalDateTime
import java.util.*

data class UserBook private constructor(
    val id: Id,
    val userId: User.Id,
    val bookId: Book.Id,
    val bookIsbn13: Book.Isbn13,
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

    fun decreaseReadingRecordCount(): UserBook {
        return this.copy(readingRecordCount = (this.readingRecordCount - 1).coerceAtLeast(0))
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
                userId = User.Id.newInstance(userId),
                bookId = Book.Id.newInstance(bookId),
                bookIsbn13 = Book.Isbn13.newInstance(bookIsbn13),
                coverImageUrl = coverImageUrl,
                publisher = publisher,
                title = title,
                author = author,
                status = status,
            )
        }

        fun reconstruct(
            id: Id,
            userId: User.Id,
            bookId: Book.Id,
            bookIsbn13: Book.Isbn13,
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
}
