package org.yapp.apis.home.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.yapp.domain.userbook.vo.HomeBookVO
import java.time.LocalDateTime
import java.util.*

@Schema(
    name = "UserHomeResponse",
    description = "사용자 홈 화면 응답 DTO"
)
data class UserHomeResponse private constructor(
    @Schema(
        description = "사용자가 최근에 읽은 책 목록", name = "recentBooks"
    ) val recentBooks: List<RecentBookResponse>
) {
    @Schema(
        description = "최근 읽은 책 한 권의 상세 정보", name = "RecentBookResponse"
    )
    data class RecentBookResponse private constructor(
        @Schema(
            description = "사용자 서재에 등록된 책의 고유 ID",
            example = "123e4567-e89b-12d3-a456-426614174000",
        ) val userBookId: UUID,

        @Schema(
            description = "책의 ISBN-13",
            example = "9788960777330",
        ) val isbn13: String,

        @Schema(
            description = "책 제목",
            example = "모던 자바스크립트 Deep Dive",
        ) val title: String,

        @Schema(
            description = "저자",
            example = "이웅모",
        ) val author: String,

        @Schema(
            description = "출판사",
            example = "위키북스",
        ) val publisher: String,

        @Schema(
            description = "책 표지 이미지 URL",
            example = "https://image.aladin.co.kr/product/2523/21/cover/8960777330_1.jpg",
        ) val coverImageUrl: String,

        @Schema(
            description = "마지막 독서 기록 시간",
            example = "2025-08-07T10:00:00",
        ) val lastRecordedAt: LocalDateTime,

        @Schema(
            description = "해당 책에 대한 총 독서 기록 수",
            example = "12",
        ) val recordCount: Int
    ) {
        companion object {
            fun from(userBookInfo: HomeBookVO): RecentBookResponse {
                return RecentBookResponse(
                    userBookId = userBookInfo.id.value,
                    isbn13 = userBookInfo.bookIsbn13.value,
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
            return UserHomeResponse(recentBooks = recentBooks.map { RecentBookResponse.from(it) })
        }
    }
}
