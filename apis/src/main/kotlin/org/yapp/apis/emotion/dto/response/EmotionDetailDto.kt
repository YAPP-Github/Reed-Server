package org.yapp.apis.emotion.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

@Schema(name = "EmotionDetailDto", description = "세부 감정")
data class EmotionDetailDto private constructor(
    @field:Schema(description = "세부 감정 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    val id: UUID,

    @field:Schema(description = "세부 감정 이름", example = "설레는")
    val name: String
) {
    companion object {
        fun of(id: UUID, name: String): EmotionDetailDto {
            return EmotionDetailDto(id = id, name = name)
        }
    }
}
