package org.yapp.apis.readingrecord.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
import org.yapp.domain.readingrecord.PrimaryEmotion
import java.util.UUID

@Schema(
    name = "UpdateReadingRecordRequestV2",
    description = "독서 기록 수정 요청 (V2)"
)
data class UpdateReadingRecordRequestV2(

    @field:Min(1, message = "페이지 번호는 1 이상이어야 합니다.")
    @field:Max(9999, message = "페이지 번호는 9999 이하여야 합니다.")
    @field:Schema(description = "현재 읽은 페이지 번호", example = "42")
    val pageNumber: Int? = null,

    @field:Size(max = 1000, message = "기억에 남는 문장은 1000자를 초과할 수 없습니다.")
    @field:Schema(description = "기억에 남는 문장", example = "이것은 기억에 남는 문장입니다.")
    val quote: String? = null,

    @field:Size(max = 1000, message = "감상평은 1000자를 초과할 수 없습니다.")
    @field:Schema(description = "감상평", example = "이 책은 매우 인상적이었습니다.")
    val review: String? = null,

    @field:Schema(description = "대분류 감정", example = "JOY")
    val primaryEmotion: PrimaryEmotion? = null,

    @field:Schema(description = "세부 감정 태그 ID 목록 (null이면 변경하지 않음)")
    val detailEmotionTagIds: List<UUID>? = null
)
