package org.yapp.apis.readingrecord.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "PrimaryEmotionDto", description = "대분류 감정")
data class PrimaryEmotionDto private constructor(
    @field:Schema(description = "감정 코드", example = "JOY")
    val code: String,

    @field:Schema(description = "감정 표시 이름", example = "즐거움")
    val displayName: String
) {
    companion object {
        fun of(code: String, displayName: String): PrimaryEmotionDto {
            return PrimaryEmotionDto(code = code, displayName = displayName)
        }
    }
}
