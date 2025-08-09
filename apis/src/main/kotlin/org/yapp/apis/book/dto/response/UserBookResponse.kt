package org.yapp.apis.book.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.userbook.vo.UserBookInfoVO
import org.yapp.globalutils.validator.BookDataValidator
import java.time.format.DateTimeFormatter
import java.util.UUID

@Schema(
    name = "UserBookResponse",
    description = "사용자 책 단일 정보 응답 DTO"
)
data class UserBookResponse private constructor(

    @field:Schema(description = "사용자 책 고유 ID(UUID)", example = "123e4567-e89b-12d3-a456-426614174000")
    val userBookId: UUID,

    @field:Schema(description = "사용자 고유 ID(UUID)", example = "987e6543-e21b-34d3-a456-426614174999")
    val userId: UUID,

    @field:Schema(description = "ISBN-13 번호", example = "9791164053353")
    val isbn13: String,

    @field:Schema(description = "책 제목", example = "데미안")
    val bookTitle: String,

    @field:Schema(description = "저자", example = "헤르만 헤세")
    val bookAuthor: String,

    @field:Schema(description = "독서 상태", example = "READING")
    val status: BookStatus,

    @field:Schema(
        description = "책 표지 이미지 URL",
        example = "https://image.aladin.co.kr/product/36801/75/coversum/k692030806_1.jpg"
    )
    val coverImageUrl: String,

    @field:Schema(description = "출판사명", example = "북하우스")
    val publisher: String,

    @field:Schema(description = "등록 일시 (ISO_LOCAL_DATE_TIME 형식)", example = "2025-08-09T15:30:00")
    val createdAt: String,

    @field:Schema(description = "수정 일시 (ISO_LOCAL_DATE_TIME 형식)", example = "2025-08-10T10:15:30")
    val updatedAt: String,

    @field:Schema(description = "독서 기록 개수", example = "15", minimum = "0")
    val recordCount: Int,
) {
    companion object {
        fun from(
            userBook: UserBookInfoVO,
        ): UserBookResponse {
            return UserBookResponse(
                userBookId = userBook.id.value,
                userId = userBook.userId.value,
                isbn13 = userBook.bookIsbn13.value,
                bookTitle = userBook.title,
                bookAuthor = BookDataValidator.removeParenthesesFromAuthor(userBook.author),
                status = userBook.status,
                coverImageUrl = userBook.coverImageUrl,
                publisher = BookDataValidator.removeParenthesesFromPublisher(userBook.publisher),
                createdAt = userBook.createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                updatedAt = userBook.updatedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                recordCount = userBook.recordCount,
            )
        }
    }
}
