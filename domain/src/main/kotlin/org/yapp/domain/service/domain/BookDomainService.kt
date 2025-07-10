package org.yapp.domain.service.domain

import org.yapp.domain.book.Book
import org.yapp.domain.book.BookRepository
import org.yapp.globalutils.annotation.DomainService

@DomainService
class BookDomainService(
    private val bookRepository: BookRepository
) {

    fun findOrCreateBook(book: Book): Book {
        return bookRepository.findByIsbn(book.isbn)
            ?: bookRepository.save(book)
    }

    fun save(book: Book): Book {
        return bookRepository.save(book)
    }
}
