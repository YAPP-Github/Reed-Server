package org.yapp.domain.book.vo

import org.yapp.domain.book.Book

data class BookVO private constructor(
    val isbn: String,
    val title: String,
    val author: String,
    val publisher: String,
    val coverImageUrl: String,
    val publicationYear: Int?,
    val description: String?
) {
    companion object {
        /**
         * ✨ BookVO를 생성하는 팩토리 메서드
         */
        fun newInstance(
            book: Book
        ): BookVO {
            return BookVO(
                book.isbn,
                book.title,
                book.author,
                book.publisher,
                book.coverImageUrl,
                book.publicationYear,
                book.description
            )
        }
    }
}
