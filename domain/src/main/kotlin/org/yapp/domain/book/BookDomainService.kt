package org.yapp.domain.book

import org.yapp.globalutils.annotation.DomainService

@DomainService
class BookDomainService(
    private val bookRepository: BookRepository
) {
    fun findByIsbn(isbn: String): Book? {
        return bookRepository.findByIsbn(isbn)
    }

    fun save(
        isbn: String,
        title: String,
        author: String,
        publisher: String,
        coverImageUrl: String,
        publicationYear: Int? = null,
        description: String? = null
    ): Book {
        findByIsbn(isbn)?.let {
            return it
        }

        val book = Book.create(
            isbn = isbn,
            title = title,
            author = author,
            publisher = publisher,
            coverImageUrl = coverImageUrl,
            publicationYear = publicationYear,
            description = description
        )

        return bookRepository.save(book)
    }
}
