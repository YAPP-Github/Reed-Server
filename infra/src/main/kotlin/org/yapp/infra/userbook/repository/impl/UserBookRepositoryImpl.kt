package org.yapp.infra.userbook.repository.impl

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.userbook.UserBookRepository
import org.yapp.domain.userbook.UserBook
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

    override fun findByIdAndUserId(id: UUID, userId: UUID): UserBook? {
        return jpaUserBookRepository.findByIdAndUserId(id, userId)?.toDomain()
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
        sort: String?,
        pageable: Pageable
    ): Page<UserBook> {
        return jpaUserBookRepository.findUserBooksByDynamicCondition(userId, status, sort, pageable)
            .map { it.toDomain() }
    }

    override fun countUserBooksByStatus(userId: UUID, status: BookStatus): Long {
        return jpaUserBookRepository.countUserBooksByStatus(userId, status)
    }
}
