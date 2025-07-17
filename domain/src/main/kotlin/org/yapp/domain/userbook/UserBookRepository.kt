package org.yapp.domain.userbook

import java.util.UUID


interface UserBookRepository {

    fun findByUserIdAndBookIsbn(userId: UUID, isbn: String): UserBook?

    fun save(userBook: UserBook): UserBook

    fun findAllByUserId(userId: UUID): List<UserBook>

    fun findAllByUserIdAndBookIsbnIn(userId: UUID, bookIsbns: List<String>): List<UserBook>

}
