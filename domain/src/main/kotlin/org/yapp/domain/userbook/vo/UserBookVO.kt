package org.yapp.domain.userbook.vo

import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.userbook.UserBook
import java.time.LocalDateTime
import java.util.UUID

data class UserBookVO private constructor(
    val id: UUID,
    val userId: UUID,
    val bookIsbn: String,
    val coverImageUrl: String,
    val publisher: String,
    val title: String,
    val author: String,
    val status: BookStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {

    companion object {
        fun newInstance(
            userBook: UserBook,
        ): UserBookVO {
            return with(userBook) {
                UserBookVO(
                    id, userId, bookIsbn, coverImageUrl,
                    publisher, title, author, status,
                    createdAt, updatedAt
                )
            }
        }
    }
}
