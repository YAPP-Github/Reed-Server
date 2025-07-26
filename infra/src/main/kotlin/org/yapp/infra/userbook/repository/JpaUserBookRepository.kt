package org.yapp.infra.userbook.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.yapp.infra.userbook.entity.UserBookEntity
import java.util.*

interface JpaUserBookRepository : JpaRepository<UserBookEntity, UUID>, JpaUserBookQuerydslRepository {
    fun findByUserIdAndBookIsbn(userId: UUID, bookIsbn: String): UserBookEntity?
    fun findByBookIdAndUserId(bookId: UUID, userId: UUID): UserBookEntity?
    fun findByIdAndUserId(id: UUID, userId: UUID): UserBookEntity?
    fun findAllByUserId(userId: UUID): List<UserBookEntity>
    fun findAllByUserIdAndBookIsbnIn(userId: UUID, bookIsbnList: List<String>): List<UserBookEntity>

}
