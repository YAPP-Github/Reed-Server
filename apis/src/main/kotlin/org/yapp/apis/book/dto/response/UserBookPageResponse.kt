package org.yapp.apis.book.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page

@Schema(description = "사용자의 책 페이지 응답")
data class UserBookPageResponse private constructor(

    @Schema(description = "책 목록 (페이지네이션)", implementation = UserBookResponse::class)
    val books: Page<UserBookResponse>,

    @Schema(description = "읽기 전 상태의 책 개수")
    val beforeReadingCount: Long,

    @Schema(description = "읽고 있는 책 개수")
    val readingCount: Long,

    @Schema(description = "완독한 책 개수")
    val completedCount: Long,

    @Schema(description = "총 책 개수")
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
