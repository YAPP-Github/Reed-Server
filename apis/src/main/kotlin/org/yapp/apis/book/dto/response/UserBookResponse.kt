package org.yapp.apis.book.dto.response

import org.yapp.domain.book.BookStatus
import org.yapp.domain.userbook.UserBook
import java.util.*

data class UserBookResponse private constructor(
    val userBookId: UUID,
    val userId: UUID,
    val bookIsbn: String,
    val bookTitle: String,
    val bookAuthor: String,
    val status: BookStatus,
    val coverImageUrl: String,
    val publisher: String,
    val createdAt: String,
    val updatedAt: String,
) {

    companion object {
        fun from(
            userBook: UserBook,
        ): UserBookResponse {
            return UserBookResponse(
                userBookId = userBook.id,
                userId = userBook.userId,
                bookIsbn = userBook.bookIsbn,
                bookTitle = userBook.title,
                bookAuthor = userBook.author,
                status = userBook.status,
                coverImageUrl = userBook.coverImageUrl,
                publisher = userBook.publisher,
                createdAt = userBook.createdAt.toString(),
                updatedAt = userBook.updatedAt.toString(),
            )
        }
    }
}
