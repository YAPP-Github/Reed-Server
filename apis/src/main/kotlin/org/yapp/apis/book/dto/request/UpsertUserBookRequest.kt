package org.yapp.apis.book.dto.request

import org.yapp.domain.userbook.BookStatus
import java.util.UUID


data class UpsertUserBookRequest private constructor(
    val userId: UUID,
    val bookIsbn: String,
    val bookTitle: String,
    val bookAuthor: String,
    val bookPublisher: String,
    val bookCoverImageUrl: String,
    val status: BookStatus
) {
    companion object {
        
        fun of(
            userId: UUID,
            bookIsbn: String,
            bookTitle: String,
            bookAuthor: String,
            bookPublisher: String,
            bookCoverImageUrl: String,
            status: BookStatus
        ): UpsertUserBookRequest {
            return UpsertUserBookRequest(
                userId = userId,
                bookIsbn = bookIsbn,
                bookTitle = bookTitle,
                bookAuthor = bookAuthor,
                bookPublisher = bookPublisher,
                bookCoverImageUrl = bookCoverImageUrl,
                status = status
            )
        }
    }
}
