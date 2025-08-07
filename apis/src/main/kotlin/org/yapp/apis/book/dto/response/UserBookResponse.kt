package org.yapp.apis.book.dto.response

import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.userbook.vo.UserBookInfoVO
import org.yapp.globalutils.validator.BookDataValidator
import java.time.format.DateTimeFormatter
import java.util.UUID

data class UserBookResponse private constructor(
    val userBookId: UUID,
    val userId: UUID,
    val isbn13: String,
    val bookTitle: String,
    val bookAuthor: String,
    val status: BookStatus,
    val coverImageUrl: String,
    val publisher: String,
    val createdAt: String,
    val updatedAt: String,
    val recordCount: Int,
) {
    companion object {
        fun from(
            userBook: UserBookInfoVO,
        ): UserBookResponse {
            return UserBookResponse(
                userBookId = userBook.id.value,
                userId = userBook.userId.value,
                isbn13 = userBook.bookIsbn13.value,
                bookTitle = userBook.title,
                bookAuthor = BookDataValidator.removeParenthesesFromAuthor(userBook.author),
                status = userBook.status,
                coverImageUrl = userBook.coverImageUrl,
                publisher = BookDataValidator.removeParenthesesFromPublisher(userBook.publisher),
                createdAt = userBook.createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                updatedAt = userBook.updatedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                recordCount = userBook.recordCount,
            )
        }
    }
}
