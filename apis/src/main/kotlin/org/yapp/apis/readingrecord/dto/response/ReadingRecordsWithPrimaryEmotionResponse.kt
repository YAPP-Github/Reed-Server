package org.yapp.apis.readingrecord.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page

@Schema(
    name = "ReadingRecordsWithPrimaryEmotionResponse",
    description = "독서 기록 목록과 대표 감정 응답"
)
data class ReadingRecordsWithPrimaryEmotionResponse private constructor(
    @field:Schema(description = "해당 책의 대표(최다) 감정")
    val representativeEmotion: PrimaryEmotionDto?,

    @field:Schema(description = "마지막 페이지 여부", example = "false")
    val lastPage: Boolean,

    @field:Schema(description = "총 결과 개수", example = "42")
    val totalResults: Int,

    @field:Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
    val startIndex: Int,

    @field:Schema(description = "한 페이지당 아이템 개수", example = "10")
    val itemsPerPage: Int,

    @field:Schema(description = "독서 기록 목록")
    val readingRecords: List<ReadingRecordResponseV2>
) {
    companion object {
        fun of(
            representativeEmotion: PrimaryEmotionDto?,
            page: Page<ReadingRecordResponseV2>
        ): ReadingRecordsWithPrimaryEmotionResponse {
            return ReadingRecordsWithPrimaryEmotionResponse(
                representativeEmotion = representativeEmotion,
                lastPage = page.isLast,
                totalResults = page.totalElements.toInt(),
                startIndex = page.number,
                itemsPerPage = page.size,
                readingRecords = page.content
            )
        }
    }
}
