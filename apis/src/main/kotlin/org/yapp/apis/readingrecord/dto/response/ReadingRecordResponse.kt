package org.yapp.apis.readingrecord.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.yapp.domain.readingrecord.vo.ReadingRecordInfoVO
import java.time.format.DateTimeFormatter
import java.util.UUID


@Schema(description = "독서 기록 응답")
data class ReadingRecordResponse private constructor(
    @Schema(description = "독서 기록 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    val id: UUID,

    @Schema(description = "사용자 책 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    val userBookId: UUID,

    @Schema(description = "현재 읽은 페이지 번호", example = "42")
    val pageNumber: Int,

    @Schema(description = "기억에 남는 문장", example = "이것은 기억에 남는 문장입니다.")
    val quote: String,

    @Schema(description = "감상평", example = "이 책은 매우 인상적이었습니다.")
    val review: String,

    @Schema(description = "감정 태그 목록", example = "[\"감동적\", \"슬픔\", \"희망\"]")
    val emotionTags: List<String>,

    @Schema(description = "생성 일시", example = "2023-01-01T12:00:00")
    val createdAt: String,

    @Schema(description = "수정 일시", example = "2023-01-01T12:00:00")
    val updatedAt: String
) {
    companion object {
        private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

        fun from(readingRecordInfoVO: ReadingRecordInfoVO): ReadingRecordResponse {
            return ReadingRecordResponse(
                id = readingRecordInfoVO.id.value,
                userBookId = readingRecordInfoVO.userBookId.value,
                pageNumber = readingRecordInfoVO.pageNumber.value,
                quote = readingRecordInfoVO.quote.value,
                review = readingRecordInfoVO.review.value,
                emotionTags = readingRecordInfoVO.emotionTags,
                createdAt = readingRecordInfoVO.createdAt.format(dateTimeFormatter),
                updatedAt = readingRecordInfoVO.updatedAt.format(dateTimeFormatter)
            )
        }
    }
}
