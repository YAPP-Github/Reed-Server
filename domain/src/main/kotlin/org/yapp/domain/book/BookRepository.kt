package org.yapp.domain.book

interface BookRepository {
    fun findById(isbn: String): Book?
    fun existsById(isbn: String): Boolean
    fun save(book: Book): Book
}
