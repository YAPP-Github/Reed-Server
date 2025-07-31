package org.yapp.domain.userbook.vo

import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.userbook.UserBook
import java.time.LocalDateTime

data class HomeBookVO private constructor(
    val id: UserBook.Id,
    val userId: UserBook.UserId,
    val bookId: UserBook.BookId,
    val bookIsbn: UserBook.BookIsbn,
    val coverImageUrl: String,
    val publisher: String,
    val title: String,
    val author: String,
    val status: BookStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val lastRecordedAt: LocalDateTime,
    val recordCount: Int
) {
    init {
        require(coverImageUrl.isNotBlank()) { "표지 이미지 URL은 비어 있을 수 없습니다." }
        require(publisher.isNotBlank()) { "출판사는 비어 있을 수 없습니다." }
        require(title.isNotBlank()) { "도서 제목은 비어 있을 수 없습니다." }
        require(author.isNotBlank()) { "저자는 비어 있을 수 없습니다." }
        require(!createdAt.isAfter(updatedAt)) {
            "생성일(createdAt)은 수정일(updatedAt)보다 이후일 수 없습니다."
        }
        require(recordCount >= 0) { "독서 기록 수는 0 이상이어야 합니다." }
    }

    companion object {
        fun newInstance(
            userBook: UserBook,
            lastRecordedAt: LocalDateTime,
            recordCount: Int
        ): HomeBookVO {
            return HomeBookVO(
                id = userBook.id,
                userId = userBook.userId,
                bookId = userBook.bookId,
                bookIsbn = userBook.bookIsbn,
                coverImageUrl = userBook.coverImageUrl,
                publisher = userBook.publisher,
                title = userBook.title,
                author = userBook.author,
                status = userBook.status,
                createdAt = userBook.createdAt ?: throw IllegalStateException("createdAt은 null일 수 없습니다."),
                updatedAt = userBook.updatedAt ?: throw IllegalStateException("updatedAt은 null일 수 없습니다."),
                lastRecordedAt = lastRecordedAt,
                recordCount = recordCount
            )
        }
    }
} 
