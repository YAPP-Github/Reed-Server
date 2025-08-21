package org.yapp.infra.userbook.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.yapp.infra.userbook.entity.UserBookEntity
import java.util.*

interface JpaUserBookRepository : JpaRepository<UserBookEntity, UUID>, JpaUserBookQuerydslRepository {
    fun findByUserIdAndBookIsbn13(userId: UUID, bookIsbn13: String): UserBookEntity?
    fun findByBookIdAndUserId(bookId: UUID, userId: UUID): UserBookEntity?
    fun existsByIdAndUserId(id: UUID, userId: UUID): Boolean
    fun findAllByUserId(userId: UUID): List<UserBookEntity>
    fun findAllByUserIdAndBookIsbn13In(userId: UUID, bookIsbn13s: List<String>): List<UserBookEntity>
}
