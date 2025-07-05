package org.yapp.infra.book.repository.impl

import org.springframework.stereotype.Repository
import org.yapp.domain.book.Book
import org.yapp.domain.book.BookRepository
import org.yapp.infra.book.entity.BookEntity
import org.yapp.infra.book.repository.JpaBookRepository

@Repository
class BookRepositoryImpl(
    private val jpaBookRepository: JpaBookRepository
) : BookRepository {

    override fun findByIsbn(isbn: String): Book? {
        return jpaBookRepository.findByIsbn(isbn)?.toDomain()
    }

    override fun save(book: Book): Book {
        val bookEntity = BookEntity.fromDomain(book)
        val savedEntity = jpaBookRepository.save(bookEntity)
        return savedEntity.toDomain()
    }
}
