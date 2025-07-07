package org.yapp.domain.book.service

import org.yapp.domain.book.Book
import org.yapp.domain.book.BookRepository
import org.yapp.globalutils.annotation.DomainService

@DomainService
class BookDomainService(
    private val bookRepository: BookRepository
) {

    fun findOrCreateBook(isbn: String, bookFactory: (String) -> Book): Book {
        return bookRepository.findByIsbn(isbn) ?: run {
            val newBook = bookFactory(isbn)
            bookRepository.save(newBook)
        }
    }
    

    fun save(book: Book): Book {
        return bookRepository.save(book)
    }
}
