package org.yapp.domain.book.vo

import org.yapp.domain.book.Book

data class BookInfoVO private constructor(
    val id: Book.Id,
    val isbn: Book.Isbn,
    val title: String,
    val author: String,
    val publisher: String,
    val coverImageUrl: String,
    val publicationYear: Int?,
    val description: String?,
) {
    init {
        require(title.isNotBlank()) { "제목은 비어 있을 수 없습니다." }
        require(author.isNotBlank()) { "저자는 비어 있을 수 없습니다." }
        publicationYear?.let { require(it > 0) { "출판 연도는 0보다 커야 합니다." } }
    }

    companion object {
        fun newInstance(
            book: Book
        ): BookInfoVO {
            return BookInfoVO(
                book.id,
                book.isbn,
                book.title,
                book.author,
                book.publisher,
                book.coverImageUrl,
                book.publicationYear,
                book.description,
            )
        }
    }
}
