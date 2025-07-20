package org.yapp.infra.userbook.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.yapp.domain.userbook.BookStatus
import org.yapp.infra.userbook.entity.UserBookEntity
import java.util.UUID

interface JpaUserBookQuerydslRepository {
    fun findUserBooksByDynamicCondition(
        userId: UUID,
        status: BookStatus?,
        sort: String?,
        pageable: Pageable
    ): Page<UserBookEntity>

    fun countUserBooksByStatus(
        userId: UUID,
        status: BookStatus
    ): Long
}
