package org.yapp.apis.book.dto.request

import org.yapp.apis.book.dto.response.BookCreateResponse
import org.yapp.apis.book.dto.response.UserBookResponse
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
            bookCreateResponse: BookCreateResponse,
            status: BookStatus
        ): UpsertUserBookRequest {
            return UpsertUserBookRequest(
                userId = userId,
                bookIsbn = bookCreateResponse.isbn,
                bookTitle = bookCreateResponse.title,
                bookAuthor = bookCreateResponse.author,
                bookPublisher = bookCreateResponse.publisher,
                bookCoverImageUrl = bookCreateResponse.coverImageUrl,
                status = status
            )
        }
    }
}
