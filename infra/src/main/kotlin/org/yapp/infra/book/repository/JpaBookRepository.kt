package org.yapp.infra.book.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.yapp.infra.book.entity.BookEntity

/**
 * JPA repository for BookEntity.
 */
import java.util.UUID

interface JpaBookRepository : JpaRepository<BookEntity, UUID> {
    fun findByIsbn(isbn: String): BookEntity?
    fun existsByIsbn(isbn: String): Boolean
}
