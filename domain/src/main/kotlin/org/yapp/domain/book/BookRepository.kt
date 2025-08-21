package org.yapp.domain.book

import java.util.UUID

interface BookRepository {
    fun findById(id: UUID): Book?
    fun existsById(id: UUID): Boolean
    fun findByIsbn13(isbn13: String): Book?
    fun existsByIsbn13(isbn13: String): Boolean
    fun save(book: Book): Book
}
