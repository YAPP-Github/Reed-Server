package org.yapp.domain.userbook

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime
import java.util.*


interface UserBookRepository {
    fun findByUserIdAndBookIsbn13(userId: UUID, isbn13: String): UserBook?
    fun findByBookIdAndUserId(bookId: UUID, userId: UUID): UserBook?
    fun existsByIdAndUserId(id: UUID, userId: UUID): Boolean
    fun findById(id: UUID): UserBook?
    fun save(userBook: UserBook): UserBook
    fun deleteById(id: UUID)
    fun findAllByUserId(userId: UUID): List<UserBook>
    fun findAllByUserIdAndBookIsbn13In(userId: UUID, bookIsbn13s: List<String>): List<UserBook>
    fun findUserBooksByDynamicCondition(
        userId: UUID,
        status: BookStatus?,
        sort: UserBookSortType?,
        title: String?,
        pageable: Pageable
    ): Page<UserBook>

    fun countUserBooksByStatus(userId: UUID, status: BookStatus): Long

    fun findRecordedBooksSortedByRecency(userId: UUID): List<Triple<UserBook, LocalDateTime, Long>>

    fun findUnrecordedBooksSortedByPriority(
        userId: UUID,
        limit: Int,
        excludeIds: Set<UUID>
    ): List<UserBook>

    /**
     * Find books registered by a user after the specified time
     * 
     * @param userId The user's ID
     * @param after Find books registered after this time
     * @return List of books matching the criteria
     */
    fun findByUserIdAndCreatedAtAfter(userId: UUID, after: LocalDateTime): List<UserBook>
}
