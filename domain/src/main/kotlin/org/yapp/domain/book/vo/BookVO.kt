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
    init {
        require(isbn.isNotBlank()) { "ISBN은 비어 있을 수 없습니다." }
        require(title.isNotBlank()) { "제목은 비어 있을 수 없습니다." }
        require(author.isNotBlank()) { "저자는 비어 있을 수 없습니다." }
        publicationYear?.let { require(it > 0) { "출판 연도는 0보다 커야 합니다." } }
    }

    companion object {
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
