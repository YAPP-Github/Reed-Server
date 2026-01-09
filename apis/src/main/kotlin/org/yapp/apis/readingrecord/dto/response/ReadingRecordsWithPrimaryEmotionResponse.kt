package org.yapp.apis.readingrecord.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page

@Schema(
    name = "ReadingRecordsWithPrimaryEmotionResponse",
    description = "독서 기록 목록과 대표 감정 응답"
)
data class ReadingRecordsWithPrimaryEmotionResponse private constructor(
    @field:Schema(description = "해당 책의 대표(최다) 감정")
    val primaryEmotion: PrimaryEmotionDto?,

    @field:Schema(description = "독서 기록 목록 (페이징)")
    val records: Page<ReadingRecordResponseV2>
) {
    companion object {
        fun of(
            primaryEmotion: PrimaryEmotionDto?,
            records: Page<ReadingRecordResponseV2>
        ): ReadingRecordsWithPrimaryEmotionResponse {
            return ReadingRecordsWithPrimaryEmotionResponse(
                primaryEmotion = primaryEmotion,
                records = records
            )
        }
    }
}
