package org.yapp.domain.userbook

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime
import java.util.*


interface UserBookRepository {

    fun findByUserIdAndBookIsbn(userId: UUID, isbn: String): UserBook?
    fun findByBookIdAndUserId(bookId: UUID, userId: UUID): UserBook?
    fun findByIdAndUserId(id: UUID, userId: UUID): UserBook?
    fun findById(id: UUID): UserBook?

    fun save(userBook: UserBook): UserBook

    fun findAllByUserId(userId: UUID): List<UserBook>

    fun findAllByUserIdAndBookIsbnIn(userId: UUID, bookIsbns: List<String>): List<UserBook>

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
}
