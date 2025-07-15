package org.yapp.infra.book.repository.impl

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.yapp.domain.book.Book
import org.yapp.domain.book.BookRepository
import org.yapp.infra.book.entity.BookEntity
import org.yapp.infra.book.repository.JpaBookRepository

@Repository
class BookRepositoryImpl(
    private val jpaBookRepository: JpaBookRepository
) : BookRepository {
    override fun findById(isbn: String): Book? {
        return jpaBookRepository.findByIdOrNull(isbn)?.toDomain()
    }

    override fun existsById(isbn: String): Boolean {
        return jpaBookRepository.existsById(isbn)
    }

    override fun save(book: Book): Book {
        val bookEntity = BookEntity.fromDomain(book)
        val savedEntity = jpaBookRepository.save(bookEntity)
        return savedEntity.toDomain()
    }
}
