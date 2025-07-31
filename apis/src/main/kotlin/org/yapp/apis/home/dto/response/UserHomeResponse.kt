package org.yapp.apis.home.dto.response

import org.yapp.domain.userbook.vo.HomeBookVO
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
        val lastRecordedAt: LocalDateTime,
        val recordCount: Int
    ) {
        companion object {
            fun from(userBookInfo: HomeBookVO): RecentBookResponse {
                return RecentBookResponse(
                    userBookId = userBookInfo.id.value,
                    title = userBookInfo.title,
                    author = userBookInfo.author,
                    publisher = userBookInfo.publisher,
                    coverImageUrl = userBookInfo.coverImageUrl,
                    lastRecordedAt = userBookInfo.lastRecordedAt,
                    recordCount = userBookInfo.recordCount
                )
            }
        }
    }

    companion object {
        fun from(recentBooks: List<HomeBookVO>): UserHomeResponse {
            return UserHomeResponse(
                recentBooks = recentBooks.map { RecentBookResponse.from(it) }
            )
        }
    }
}
