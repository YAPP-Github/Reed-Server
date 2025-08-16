package org.yapp.infra.userbook.repository.impl

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.userbook.UserBook
import org.yapp.domain.userbook.UserBookRepository
import org.yapp.domain.userbook.UserBookSortType
import org.yapp.infra.userbook.entity.UserBookEntity
import org.yapp.infra.userbook.repository.JpaUserBookRepository
import java.time.LocalDateTime
import java.util.*

@Repository
class UserBookRepositoryImpl(
    private val jpaUserBookRepository: JpaUserBookRepository
) : UserBookRepository {

    override fun findByUserIdAndBookIsbn13(userId: UUID, isbn13: String): UserBook? {
        return jpaUserBookRepository.findByUserIdAndBookIsbn13(userId, isbn13)?.toDomain()
    }

    override fun findByBookIdAndUserId(bookId: UUID, userId: UUID): UserBook? {
        return jpaUserBookRepository.findByBookIdAndUserId(bookId, userId)?.toDomain()
    }

    override fun existsByIdAndUserId(id: UUID, userId: UUID): Boolean {
        return jpaUserBookRepository.existsByIdAndUserId(id, userId)
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

    override fun findAllByUserIdAndBookIsbn13In(
        userId: UUID,
        bookIsbn13s: List<String>
    ): List<UserBook> {
        return jpaUserBookRepository.findAllByUserIdAndBookIsbn13In(userId, bookIsbn13s)
            .map { it.toDomain() }
    }

    override fun findUserBooksByDynamicCondition(
        userId: UUID,
        status: BookStatus?,
        sort: UserBookSortType?,
        title: String?,
        pageable: Pageable
    ): Page<UserBook> {
        val page = jpaUserBookRepository.findUserBooksByDynamicCondition(userId, status, sort, title, pageable)
        return page.map { it.toDomain() }
    }

    override fun countUserBooksByStatus(userId: UUID, status: BookStatus): Long {
        return jpaUserBookRepository.countUserBooksByStatus(userId, status)
    }

    override fun findRecordedBooksSortedByRecency(userId: UUID): List<Triple<UserBook, LocalDateTime, Long>> {
        val userBookLastRecordsProjections = jpaUserBookRepository.findRecordedBooksSortedByRecency(userId)

        return userBookLastRecordsProjections.map { projection ->
            Triple(projection.userBookEntity.toDomain(), projection.lastRecordedAt, projection.recordCount)
        }
    }

    override fun findUnrecordedBooksSortedByPriority(
        userId: UUID,
        limit: Int,
        excludeIds: Set<UUID>
    ): List<UserBook> {
        val entities = jpaUserBookRepository.findUnrecordedBooksSortedByPriority(userId, excludeIds, limit)
        return entities.map { it.toDomain() }
    }


}
