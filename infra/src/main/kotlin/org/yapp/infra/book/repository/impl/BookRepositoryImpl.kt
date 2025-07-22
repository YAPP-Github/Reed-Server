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
    override fun findById(id: String): Book? {
        return jpaBookRepository.findByIdOrNull(id)?.toDomain()
    }

    override fun existsById(id: String): Boolean {
        return jpaBookRepository.existsById(id)
    }

    override fun save(book: Book): Book {
        val savedEntity = jpaBookRepository.saveAndFlush(BookEntity.fromDomain(book))
        return savedEntity.toDomain()
    }
}
