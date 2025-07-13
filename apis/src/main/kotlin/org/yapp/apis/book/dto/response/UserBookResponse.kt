package org.yapp.apis.book.dto.response

import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.userbook.UserBook
import org.yapp.domain.userbook.vo.UserBookVO
import java.time.format.DateTimeFormatter
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
        fun from(userBook: UserBook): UserBookResponse {
            return UserBookResponse(
                userBookId = userBook.id,
                userId = userBook.userId,
                bookIsbn = userBook.bookIsbn,
                bookTitle = userBook.title,
                bookAuthor = userBook.author,
                status = userBook.status,
                coverImageUrl = userBook.coverImageUrl,
                publisher = userBook.publisher,
                createdAt = userBook.createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                updatedAt = userBook.updatedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            )
        }

        fun from(vo: UserBookVO): UserBookResponse {
            return UserBookResponse(
                userBookId = vo.id,
                userId = vo.userId,
                bookIsbn = vo.bookIsbn,
                bookTitle = vo.title,
                bookAuthor = vo.author,
                status = vo.status,
                coverImageUrl = vo.coverImageUrl,
                publisher = vo.publisher,
                createdAt = vo.createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                updatedAt = vo.updatedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            )
        }
    }
}
