package org.yapp.apis.book.dto.response

import org.yapp.domain.book.vo.BookInfoVO

data class BookCreateResponse private constructor(
    val isbn: String,
    val title: String,
    val author: String,
    val publisher: String,
    val coverImageUrl: String
) {
    companion object {
        fun from(bookVO: BookInfoVO): BookCreateResponse {
            return BookCreateResponse(
                isbn = bookVO.isbn.value,
                title = bookVO.title,
                author = bookVO.author,
                publisher = bookVO.publisher,
                coverImageUrl = bookVO.coverImageUrl
            )
        }
    }
}
