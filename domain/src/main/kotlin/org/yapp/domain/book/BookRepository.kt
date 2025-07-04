package org.yapp.domain.book

/**
 * Repository interface for Book domain model.
 */
interface BookRepository {

    fun findByIsbn(isbn: String): Book?

    fun save(book: Book): Book
}
