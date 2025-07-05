package org.yapp.infra.book.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.yapp.infra.book.entity.BookEntity

/**
 * JPA repository for BookEntity.
 */
interface JpaBookRepository : JpaRepository<BookEntity, String> {

    fun findByIsbn(isbn: String): BookEntity?
}
