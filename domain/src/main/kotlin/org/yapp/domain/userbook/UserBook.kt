package org.yapp.domain.userbook

import org.yapp.globalutils.util.UuidGenerator
import java.time.LocalDateTime
import java.util.*

data class UserBook private constructor(
    val id: Id,
    val userId: UserId,
    val bookIsbn: BookIsbn,
    val coverImageUrl: String,
    val publisher: String,
    val title: String,
    val author: String,
    val status: BookStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime? = null,
) {
    fun updateStatus(newStatus: BookStatus): UserBook {
        return this.copy(status = newStatus, updatedAt = LocalDateTime.now())
    }

    companion object {
        fun create(
            userId: UUID,
            bookIsbn: String,
            coverImageUrl: String,
            publisher: String,
            title: String,
            author: String,
            initialStatus: BookStatus = BookStatus.BEFORE_READING
        ): UserBook {
            val now = LocalDateTime.now()
            return UserBook(
                id = Id.newInstance(UuidGenerator.create()),
                userId = UserId.newInstance(userId),
                bookIsbn = BookIsbn.newInstance(bookIsbn),
                coverImageUrl = coverImageUrl,
                publisher = publisher,
                title = title,
                author = author,
                status = initialStatus,
                createdAt = now,
                updatedAt = now
            )
        }

        fun reconstruct(
            id: Id,
            userId: UserId,
            bookIsbn: BookIsbn,
            coverImageUrl: String,
            publisher: String,
            title: String,
            author: String,
            status: BookStatus,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime,
            deletedAt: LocalDateTime?
        ): UserBook {
            return UserBook(
                id = id,
                userId = userId,
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
    value class BookIsbn(val value: String) {
        companion object {
            fun newInstance(value: String): BookIsbn {
                require(value.matches(Regex("^(\\d{10}|\\d{13})$"))) { "ISBN은 10자리 또는 13자리 숫자여야 합니다." }
                return BookIsbn(value)
            }
        }
    }
}
