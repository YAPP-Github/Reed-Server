package org.yapp.domain.book

import org.yapp.domain.book.vo.BookInfoVO
import org.yapp.globalutils.annotation.DomainService

@DomainService
class BookDomainService(
    private val bookRepository: BookRepository
) {
    fun findByIsbn(isbn: String): BookInfoVO? {
        return bookRepository.findByIsbn(isbn)?.let { BookInfoVO.newInstance(it) }
    }

    fun save(
        isbn: String,
        title: String,
        author: String,
        publisher: String,
        coverImageUrl: String,
        publicationYear: Int? = null,
        description: String? = null
    ): BookInfoVO {
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
        return BookInfoVO.newInstance(savedBook)
    }
}
