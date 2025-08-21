package org.yapp.infra.book.repository.impl

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.yapp.domain.book.Book
import org.yapp.domain.book.BookRepository
import org.yapp.infra.book.entity.BookEntity
import org.yapp.infra.book.repository.JpaBookRepository
import java.util.UUID

@Repository
class BookRepositoryImpl(
    private val jpaBookRepository: JpaBookRepository
) : BookRepository {
    override fun findById(id: UUID): Book? {
        return jpaBookRepository.findByIdOrNull(id)?.toDomain()
    }

    override fun existsById(id: UUID): Boolean {
        return jpaBookRepository.existsById(id)
    }

    override fun findByIsbn13(isbn13: String): Book? {
        return jpaBookRepository.findByIsbn13(isbn13)?.toDomain()
    }

    override fun existsByIsbn13(isbn13: String): Boolean {
        return jpaBookRepository.existsByIsbn13(isbn13)
    }

    override fun save(book: Book): Book {
        val savedEntity = jpaBookRepository.saveAndFlush(BookEntity.fromDomain(book))
        return savedEntity.toDomain()
    }
}
