package org.yapp.apis.book.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.domain.book.Book

data class BookCreateRequest private constructor(
    @field:NotBlank(message = "ISBN은 필수입니다.")
    val isbn: String? = null,

    @field:NotBlank(message = "제목은 필수입니다.")
    val title: String? = null,

    @field:NotBlank(message = "저자는 필수입니다.")
    val author: String? = null,

    @field:NotBlank(message = "출판사는 필수입니다.")
    val publisher: String? = null,

    val publicationYear: Int? = null,

    @field:Size(max = 2048, message = "표지 URL은 2048자 이내여야 합니다.")
    val coverImageUrl: String,

    val description: String? = null,
) {
    fun validIsbn(): String = isbn!!
    fun validTitle(): String = title!!
    fun validAuthor(): String = author!!
    fun validPublisher(): String = publisher!!

    companion object {

        fun create(bookDetail: BookDetailResponse): BookCreateRequest {
            val finalIsbn = bookDetail.isbn ?: bookDetail.isbn13
            ?: throw IllegalArgumentException("ISBN이 존재하지 않습니다.")

            return BookCreateRequest(
                isbn = finalIsbn,
                title = bookDetail.title,
                author = bookDetail.author,
                publisher = bookDetail.publisher,
                publicationYear = parsePublicationYear(bookDetail.pubDate),
                coverImageUrl = bookDetail.cover,
                description = bookDetail.description
            )
        }

        private fun parsePublicationYear(pubDate: String?): Int? {
            return pubDate
                ?.takeIf { it.length >= 4 && it.substring(0, 4).all { ch -> ch.isDigit() } }
                ?.substring(0, 4)
                ?.toIntOrNull()
        }
    }
}
