package org.yapp.domain.userbook // UserBook 도메인 모델의 올바른 패키지

import org.yapp.domain.book.Book
import org.yapp.domain.book.BookStatus
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
    var status: BookStatus,
    var createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
    var deletedAt: LocalDateTime? = null,
) {
    fun updateStatus(newStatus: BookStatus): UserBook {
        this.status = newStatus
        this.updatedAt = LocalDateTime.now()
        return this
    }


    companion object {
        fun create(
            userId: UUID,
            book: Book,
            initialStatus: BookStatus = BookStatus.BEFORE_READING
        ): UserBook {
            val now = LocalDateTime.now()
            return UserBook(
                id = UuidGenerator.create(),
                coverImageUrl = book.coverImageUrl,
                publisher = book.publisher,
                title = book.title,
                author = book.author,
                userId = userId,
                bookIsbn = book.isbn,
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
            createdAt: LocalDateTime = LocalDateTime.now(),
            updatedAt: LocalDateTime = LocalDateTime.now(),
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
