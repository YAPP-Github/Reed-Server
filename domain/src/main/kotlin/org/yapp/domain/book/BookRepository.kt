package org.yapp.domain.book

import java.util.UUID

interface BookRepository {
    fun findById(id: UUID): Book?
    fun existsById(id: UUID): Boolean
    fun findByIsbn(isbn: String): Book?
    fun existsByIsbn(isbn: String): Boolean
    fun save(book: Book): Book
}
