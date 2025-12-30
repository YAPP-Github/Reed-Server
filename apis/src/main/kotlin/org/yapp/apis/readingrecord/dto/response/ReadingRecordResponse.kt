package org.yapp.apis.readingrecord.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.yapp.domain.readingrecord.vo.ReadingRecordInfoVO
import java.time.format.DateTimeFormatter
import java.util.UUID


@Schema(
    name = "ReadingRecordResponse",
    description = "독서 기록 응답"
)
data class ReadingRecordResponse private constructor(
    @field:Schema(description = "독서 기록 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    val id: UUID,

    @field:Schema(description = "사용자 책 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    val userBookId: UUID,

    @field:Schema(description = "현재 읽은 페이지 번호 (선택)", example = "42")
    val pageNumber: Int?,

    @field:Schema(description = "기억에 남는 문장", example = "이것은 기억에 남는 문장입니다.")
    val quote: String,

    @field:Schema(description = "감상평", example = "이 책은 매우 인상적이었습니다.")
    val review: String?,

    @field:Schema(description = "감정 태그 목록", example = "[\"감동적\", \"슬픔\", \"희망\"]")
    val emotionTags: List<String>,

    @field:Schema(description = "생성 일시", example = "2023-01-01T12:00:00")
    val createdAt: String,

    @field:Schema(description = "수정 일시", example = "2023-01-01T12:00:00")
    val updatedAt: String,

    @field:Schema(description = "도서 제목", example = "클린 코드")
    val bookTitle: String?,

    @field:Schema(description = "출판사", example = "인사이트")
    val bookPublisher: String?,

    @field:Schema(description = "도서 썸네일 URL", example = "https://example.com/book-cover.jpg")
    val bookCoverImageUrl: String?,

    @field:Schema(description = "저자", example = "로버트 C. 마틴")
    val author: String?
) {
    companion object {
        private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

        fun from(readingRecordInfoVO: ReadingRecordInfoVO): ReadingRecordResponse {
            return ReadingRecordResponse(
                id = readingRecordInfoVO.id.value,
                userBookId = readingRecordInfoVO.userBookId.value,
                pageNumber = readingRecordInfoVO.pageNumber?.value,
                quote = readingRecordInfoVO.quote.value,
                review = readingRecordInfoVO.review?.value,
                emotionTags = readingRecordInfoVO.emotionTags,
                createdAt = readingRecordInfoVO.createdAt.format(dateTimeFormatter),
                updatedAt = readingRecordInfoVO.updatedAt.format(dateTimeFormatter),
                bookTitle = readingRecordInfoVO.bookTitle,
                bookPublisher = readingRecordInfoVO.bookPublisher,
                bookCoverImageUrl = readingRecordInfoVO.bookCoverImageUrl,
                author = readingRecordInfoVO.author
            )
        }
    }
}

