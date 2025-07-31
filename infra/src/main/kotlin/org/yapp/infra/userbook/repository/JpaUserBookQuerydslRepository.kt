package org.yapp.infra.userbook.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.userbook.UserBookSortType
import org.yapp.infra.userbook.entity.UserBookEntity
import org.yapp.infra.userbook.repository.dto.UserBookLastRecordProjection
import java.util.*

interface JpaUserBookQuerydslRepository {
    fun findUserBooksByDynamicCondition(
        userId: UUID,
        status: BookStatus?,
        sort: UserBookSortType?,
        pageable: Pageable
    ): Page<UserBookEntity>

    fun countUserBooksByStatus(
        userId: UUID,
        status: BookStatus
    ): Long

    fun findRecordedBooksSortedByRecency(userId: UUID): List<UserBookLastRecordProjection>

    fun findUnrecordedBooksSortedByPriority(
        userId: UUID,
        excludeIds: Set<UUID>,
        limit: Int
    ): List<UserBookEntity>
}
