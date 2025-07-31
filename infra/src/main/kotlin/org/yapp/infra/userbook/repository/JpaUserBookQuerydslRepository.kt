package org.yapp.infra.userbook.repository

import com.querydsl.core.Tuple
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.userbook.UserBookSortType
import org.yapp.infra.userbook.entity.UserBookEntity
import java.util.UUID

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

    fun findUserBooksWithLastRecord(
        userId: UUID,
        limit: Int
    ): List<Tuple>
}
