package org.yapp.domain.userbook.vo

import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.userbook.UserBook
import java.time.LocalDateTime
import java.util.UUID

data class UserBookVO private constructor(
    val id: UUID,
    val userId: UUID,
    val bookIsbn: String,
    val coverImageUrl: String,
    val publisher: String,
    val title: String,
    val author: String,
    val status: BookStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {

    companion object {
        fun newInstance(
            userBook: UserBook,
        ): UserBookVO {
            return UserBookVO(
                id = userBook.id,
                userId = userBook.userId,
                bookIsbn = userBook.bookIsbn,
                coverImageUrl = userBook.coverImageUrl,
                publisher = userBook.publisher,
                title = userBook.title,
                author = userBook.author,
                status = userBook.status,
                createdAt = userBook.createdAt,
                updatedAt = userBook.updatedAt
            )
        }
    }
}
