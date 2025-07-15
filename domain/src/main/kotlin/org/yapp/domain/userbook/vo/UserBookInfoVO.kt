package org.yapp.domain.userbook.vo

import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.userbook.UserBook
import java.time.LocalDateTime
import java.util.UUID

data class UserBookInfoVO private constructor(
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
    init {
        require(bookIsbn.isNotBlank()) { "도서 ISBN은 비어 있을 수 없습니다." }
        require(coverImageUrl.isNotBlank()) { "표지 이미지 URL은 비어 있을 수 없습니다." }
        require(publisher.isNotBlank()) { "출판사는 비어 있을 수 없습니다." }
        require(title.isNotBlank()) { "도서 제목은 비어 있을 수 없습니다." }
        require(author.isNotBlank()) { "저자는 비어 있을 수 없습니다." }
        require(createdAt.isBefore(updatedAt) || createdAt == updatedAt) {
            "생성일(createdAt)은 수정일(updatedAt)보다 이후일 수 없습니다."
        }
    }

    companion object {
        fun newInstance(
            userBook: UserBook,
        ): UserBookInfoVO {
            return UserBookInfoVO(
                id = userBook.id,
                userId = userBook.userId,
                bookIsbn = userBook.bookIsbn,
                coverImageUrl = userBook.coverImageUrl,
                publisher = userBook.publisher,
                title = userBook.title,
                author = userBook.author,
                status = userBook.status,
                createdAt = userBook.createdAt,
                updatedAt = userBook.updatedAt
            )
        }
    }
}
