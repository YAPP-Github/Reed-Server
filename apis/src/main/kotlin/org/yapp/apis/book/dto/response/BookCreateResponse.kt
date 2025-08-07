package org.yapp.apis.book.dto.response

import org.yapp.domain.book.vo.BookInfoVO

import java.util.UUID

data class BookCreateResponse private constructor(
    val bookId: UUID,
    val isbn13: String,
    val title: String,
    val author: String,
    val publisher: String,
    val coverImageUrl: String,
) {
    companion object {
        fun from(bookVO: BookInfoVO): BookCreateResponse {
            return BookCreateResponse(
                bookId = bookVO.id.value,
                isbn13 = bookVO.isbn13.value,
                title = bookVO.title,
                author = bookVO.author,
                publisher = bookVO.publisher,
                coverImageUrl = bookVO.coverImageUrl,
            )
        }
    }
}
