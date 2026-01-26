package org.yapp.apis.emotion.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.yapp.domain.detailtag.DetailTag
import org.yapp.domain.readingrecord.PrimaryEmotion
import java.util.UUID

@Schema(name = "EmotionListResponse", description = "감정 목록 응답")
data class EmotionListResponse private constructor(
    @field:Schema(description = "감정 그룹 목록")
    val emotions: List<EmotionGroupDto>
) {
    companion object {
        fun from(detailTags: List<DetailTag>): EmotionListResponse {
            val grouped = detailTags.groupBy { it.primaryEmotion }

            val emotions = PrimaryEmotion.entries.map { primary ->
                EmotionGroupDto.of(
                    code = primary.name,
                    displayName = primary.displayName,
                    detailEmotions = grouped[primary]
                        ?.sortedBy { it.displayOrder }
                        ?.map { EmotionDetailDto.of(id = it.id.value, name = it.name) }
                        ?: emptyList()
                )
            }

            return EmotionListResponse(emotions = emotions)
        }
    }

    @Schema(name = "EmotionGroupDto", description = "감정 그룹 (대분류 + 세부감정)")
    data class EmotionGroupDto private constructor(
        @field:Schema(description = "대분류 코드", example = "JOY")
        val code: String,

        @field:Schema(description = "대분류 표시 이름", example = "즐거움")
        val displayName: String,

        @field:Schema(description = "세부 감정 목록")
        val detailEmotions: List<EmotionDetailDto>
    ) {
        companion object {
            fun of(
                code: String,
                displayName: String,
                detailEmotions: List<EmotionDetailDto>
            ): EmotionGroupDto {
                return EmotionGroupDto(
                    code = code,
                    displayName = displayName,
                    detailEmotions = detailEmotions
                )
            }
        }
    }

}
