package org.yapp.domain.book

interface BookRepository {
    fun findById(id: String): Book?
    fun existsById(id: String): Boolean
    fun save(book: Book): Book
}
