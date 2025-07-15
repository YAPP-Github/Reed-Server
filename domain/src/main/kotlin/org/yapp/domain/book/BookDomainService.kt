package org.yapp.domain.book

import org.yapp.domain.book.vo.BookVO
import org.yapp.globalutils.annotation.DomainService

@DomainService
class BookDomainService(
    private val bookRepository: BookRepository
) {
    fun findByIsbn(isbn: String): BookVO? {
        return bookRepository.findByIsbn(isbn)?.let { BookVO.newInstance(it) }
    }

    fun save(
        isbn: String,
        title: String,
        author: String,
        publisher: String,
        coverImageUrl: String,
        publicationYear: Int? = null,
        description: String? = null
    ): BookVO {
        val book = bookRepository.findByIsbn(isbn) ?: Book.create(
            isbn = isbn,
            title = title,
            author = author,
            publisher = publisher,
            coverImageUrl = coverImageUrl,
            publicationYear = publicationYear,
            description = description
        )

        val savedBook = bookRepository.save(book)
        return BookVO.newInstance(savedBook)
    }
}
