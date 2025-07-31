package org.yapp.infra.userbook.repository.dto

import com.querydsl.core.annotations.QueryProjection
import org.yapp.infra.userbook.entity.UserBookEntity
import java.time.LocalDateTime

data class UserBookLastRecordProjection @QueryProjection constructor(
    val userBookEntity: UserBookEntity,
    val lastRecordedAt: LocalDateTime,
    val recordCount: Long
)
