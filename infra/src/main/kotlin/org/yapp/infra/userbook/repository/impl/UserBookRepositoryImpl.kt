package org.yapp.infra.userbook.repository.impl

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.userbook.UserBookRepository
import org.yapp.domain.userbook.UserBook
import org.yapp.domain.userbook.UserBookSortType
import org.yapp.infra.userbook.entity.UserBookEntity
import org.yapp.infra.userbook.repository.JpaUserBookRepository
import java.util.*

@Repository
class UserBookRepositoryImpl(
    private val jpaUserBookRepository: JpaUserBookRepository
) : UserBookRepository {

    override fun findByUserIdAndBookIsbn(userId: UUID, isbn: String): UserBook? {
        return jpaUserBookRepository.findByUserIdAndBookIsbn(userId, isbn)?.toDomain()
    }

    override fun findByBookIdAndUserId(bookId: UUID, userId: UUID): UserBook? {
        return jpaUserBookRepository.findByBookIdAndUserId(bookId, userId)?.toDomain()
    }

    override fun findByIdAndUserId(id: UUID, userId: UUID): UserBook? {
        return jpaUserBookRepository.findByIdAndUserId(id, userId)?.toDomain()
    }

    override fun findById(id: UUID): UserBook? {
        return jpaUserBookRepository.findById(id).orElse(null)?.toDomain()
    }

    override fun save(userBook: UserBook): UserBook {
        val savedEntity = jpaUserBookRepository.saveAndFlush(UserBookEntity.fromDomain(userBook))
        return savedEntity.toDomain()
    }

    override fun findAllByUserId(userId: UUID): List<UserBook> {
        return jpaUserBookRepository.findAllByUserId(userId).map { it.toDomain() }
    }

    override fun findAllByUserIdAndBookIsbnIn(
        userId: UUID,
        bookIsbns: List<String>
    ): List<UserBook> {
        return jpaUserBookRepository.findAllByUserIdAndBookIsbnIn(userId, bookIsbns)
            .map { it.toDomain() }
    }

    override fun findUserBooksByDynamicCondition(
        userId: UUID,
        status: BookStatus?,
        sort: UserBookSortType?,
        title: String?,
        pageable: Pageable
    ): Page<UserBook> {
        return jpaUserBookRepository.findUserBooksByDynamicCondition(userId, status, sort, title, pageable)
            .map { it.toDomain() }
    }

    override fun countUserBooksByStatus(userId: UUID, status: BookStatus): Long {
        return jpaUserBookRepository.countUserBooksByStatus(userId, status)
    }
}
