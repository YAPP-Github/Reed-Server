package org.yapp.apis.home.dto.response

import org.yapp.domain.userbook.vo.UserBookWithLastRecordVO
import java.time.LocalDateTime
import java.util.*

data class UserHomeResponse private constructor(
    val recentBooks: List<RecentBookResponse>
) {
    data class RecentBookResponse(
        val userBookId: UUID,
        val title: String,
        val author: String,
        val publisher: String,
        val coverImageUrl: String,
        val lastRecordedAt: LocalDateTime?
    ) {
        companion object {
            fun from(userBookInfo: UserBookWithLastRecordVO): RecentBookResponse {
                return RecentBookResponse(
                    userBookId = userBookInfo.id.value,
                    title = userBookInfo.title,
                    author = userBookInfo.author,
                    publisher = userBookInfo.publisher,
                    coverImageUrl = userBookInfo.coverImageUrl,
                    lastRecordedAt = userBookInfo.lastRecordedAt
                )
            }
        }
    }

    companion object {
        fun from(recentBooks: List<UserBookWithLastRecordVO>): UserHomeResponse {
            return UserHomeResponse(
                recentBooks = recentBooks.map { RecentBookResponse.from(it) }
            )
        }
    }
}
