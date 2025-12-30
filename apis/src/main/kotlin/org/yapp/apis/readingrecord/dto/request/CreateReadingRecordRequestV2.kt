package org.yapp.apis.readingrecord.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.yapp.domain.readingrecord.PrimaryEmotion
import java.util.UUID

@Schema(
    name = "CreateReadingRecordRequestV2",
    description = "독서 기록 생성 요청 (V2)",
    example = """
        {
          "pageNumber": 42,
          "quote": "이것은 기억에 남는 문장입니다.",
          "review": "이 책은 매우 인상적이었습니다.",
          "primaryEmotion": "JOY",
          "detailEmotionTagIds": ["uuid-1", "uuid-2"]
        }
    """
)
data class CreateReadingRecordRequestV2 private constructor(

    @field:Min(1, message = "페이지 번호는 1 이상이어야 합니다.")
    @field:Max(9999, message = "페이지 번호는 9999 이하여야 합니다.")
    @field:Schema(description = "현재 읽은 페이지 번호", example = "42", required = false)
    val pageNumber: Int? = null,

    @field:NotBlank(message = "기억에 남는 문장은 필수입니다.")
    @field:Size(max = 1000, message = "기억에 남는 문장은 1000자를 초과할 수 없습니다.")
    @field:Schema(description = "기억에 남는 문장", example = "이것은 기억에 남는 문장입니다.", required = true)
    val quote: String? = null,

    @field:Size(max = 1000, message = "감상평은 1000자를 초과할 수 없습니다.")
    @field:Schema(description = "감상평", example = "이 책은 매우 인상적이었습니다.", required = false)
    val review: String? = null,

    @field:NotNull(message = "대분류 감정은 필수입니다.")
    @field:Schema(description = "대분류 감정", example = "JOY", required = true)
    val primaryEmotion: PrimaryEmotion? = null,

    @field:Schema(description = "세부 감정 태그 ID 목록 (선택, 다중 선택 가능)", example = "[\"uuid-1\", \"uuid-2\"]")
    val detailEmotionTagIds: List<UUID> = emptyList()
) {
    fun validQuote(): String =
        requireNotNull(quote) { "quote는 null일 수 없습니다." }

    fun validPrimaryEmotion(): PrimaryEmotion =
        requireNotNull(primaryEmotion) { "primaryEmotion은 null일 수 없습니다." }
}
