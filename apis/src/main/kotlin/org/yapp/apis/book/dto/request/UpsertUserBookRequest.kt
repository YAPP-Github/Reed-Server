package org.yapp.apis.book.dto.request

import org.yapp.apis.book.dto.response.BookCreateResponse
import org.yapp.apis.book.dto.response.UserBookResponse
import org.yapp.domain.userbook.BookStatus
import java.util.UUID


data class UpsertUserBookRequest private constructor(
    val userId: UUID? = null,
    val bookIsbn: String? = null,
    val bookTitle: String? = null,
    val bookAuthor: String? = null,
    val bookPublisher: String? = null,
    val bookCoverImageUrl: String? = null,
    val status: BookStatus? = null
) {
    fun validUserId(): UUID = userId!!
    fun validBookIsbn(): String = bookIsbn!!
    fun validBookTitle(): String = bookTitle!!
    fun validBookAuthor(): String = bookAuthor!!
    fun validBookPublisher(): String = bookPublisher!!
    fun validBookCoverImageUrl(): String = bookCoverImageUrl!!
    fun validStatus(): BookStatus = status!!

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
