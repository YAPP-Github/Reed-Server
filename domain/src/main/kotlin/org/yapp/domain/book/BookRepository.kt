package org.yapp.domain.book

interface BookRepository {
    fun findByIsbn(isbn: String): Book?
    fun save(book: Book): Book
}
