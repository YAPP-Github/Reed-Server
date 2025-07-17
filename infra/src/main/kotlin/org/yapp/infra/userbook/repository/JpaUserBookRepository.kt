package org.yapp.infra.userbook.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.yapp.infra.userbook.entity.UserBookEntity
import java.util.*

interface JpaUserBookRepository : JpaRepository<UserBookEntity, UUID> {
    fun findByUserIdAndBookIsbn(userId: UUID, bookIsbn: String): UserBookEntity?
    fun findAllByUserId(userId: UUID): List<UserBookEntity>
}
