package org.yapp.apis.book.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page

@Schema(
    name = "UserBookPageResponse",
    description = "사용자의 책 목록 페이징 응답 데이터"
)
data class UserBookPageResponse private constructor(

    @Schema(
        description = "페이징된 책 목록",
        implementation = UserBookResponse::class
    )
    val books: Page<UserBookResponse>,

    @Schema(description = "읽기 전 상태의 책 개수", example = "5")
    val beforeReadingCount: Long,

    @Schema(description = "현재 읽고 있는 책 개수", example = "3")
    val readingCount: Long,

    @Schema(description = "완독한 책 개수", example = "10")
    val completedCount: Long,

    @Schema(description = "전체 책 개수", example = "18")
    val totalCount: Long
) {
    companion object {
        fun of(
            books: Page<UserBookResponse>,
            beforeReadingCount: Long,
            readingCount: Long,
            completedCount: Long
        ): UserBookPageResponse {
            val totalCount = beforeReadingCount + readingCount + completedCount
            return UserBookPageResponse(
                books = books,
                beforeReadingCount = beforeReadingCount,
                readingCount = readingCount,
                completedCount = completedCount,
                totalCount = totalCount
            )
        }
    }
}
