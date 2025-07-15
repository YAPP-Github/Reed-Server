package org.yapp.domain.userbook // UserBook 도메인 모델의 올바른 패키지

import org.yapp.domain.book.Book
import org.yapp.globalutils.util.UuidGenerator
import java.time.LocalDateTime
import java.util.*


data class UserBook private constructor(
    val id: UUID,
    val userId: UUID,
    val bookIsbn: String,
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
            coverImageUrl: String,
            bookIsbn: String,
            publisher: String,
            title: String,
            author: String,
            initialStatus: BookStatus = BookStatus.BEFORE_READING
        ): UserBook {
            val now = LocalDateTime.now()
            return UserBook(
                id = UuidGenerator.create(),
                coverImageUrl = coverImageUrl,
                publisher = publisher,
                title = title,
                author = author,
                userId = userId,
                bookIsbn = bookIsbn,
                status = initialStatus,
                createdAt = now,
                updatedAt = now,
                deletedAt = null,
            )
        }

        fun reconstruct(
            id: UUID,
            userId: UUID,
            bookIsbn: String,
            status: BookStatus,
            coverImageUrl: String,
            title: String,
            author: String,
            publisher: String,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime,
            deletedAt: LocalDateTime? = null,
        ): UserBook {
            return UserBook(
                id = id,
                userId = userId,
                bookIsbn = bookIsbn,
                status = status,
                coverImageUrl = coverImageUrl,
                title = title,
                author = author,
                publisher = publisher,
                createdAt = createdAt,
                updatedAt = updatedAt,
                deletedAt = deletedAt,
            )
        }
    }
}
