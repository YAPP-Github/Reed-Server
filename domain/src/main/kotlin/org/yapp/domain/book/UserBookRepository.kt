package org.yapp.domain.book

import org.yapp.domain.userbook.UserBook
import java.util.*


interface UserBookRepository {

    fun findByUserIdAndBookIsbn(userId: UUID, isbn: String): UserBook?

    fun save(userBook: UserBook): UserBook

    fun findAllByUserId(userId: UUID): List<UserBook>
}
