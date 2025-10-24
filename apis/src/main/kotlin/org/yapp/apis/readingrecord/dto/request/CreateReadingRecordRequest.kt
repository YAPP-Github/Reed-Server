package org.yapp.apis.readingrecord.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(
    name = "CreateReadingRecordRequest",
    description = "독서 기록 생성 요청",
    example = """
        {
          "pageNumber": 42,
          "quote": "이것은 기억에 남는 문장입니다.",
          "review": "이 책은 매우 인상적이었습니다.",
          "emotionTags": ["감동적"]
        }
    """
)
data class CreateReadingRecordRequest private constructor(

    @field:Min(1, message = "페이지 번호는 1 이상이어야 합니다.")
    @field:Max(9999, message = "페이지 번호는 9999 이하여야 합니다.")
    @field:Schema(description = "현재 읽은 페이지 번호", example = "42", required = true)
    val pageNumber: Int? = null,

    @field:NotBlank(message = "기억에 남는 문장은 필수입니다.")
    @field:Size(max = 1000, message = "기억에 남는 문장은 1000자를 초과할 수 없습니다.")
    @field:Schema(description = "기억에 남는 문장", example = "이것은 기억에 남는 문장입니다.", required = true)
    val quote: String? = null,

    @field:Size(max = 1000, message = "감상평은 1000자를 초과할 수 없습니다.")
    @field:Schema(description = "감상평", example = "이 책은 매우 인상적이었습니다.", required = false)
    val review: String? = null,

    @field:Size(max = 1, message = "감정 태그는 최대 1개까지 가능합니다. (단일 감정만 받지만, 확장성을 위해 리스트 형태로 관리됩니다.)")
    @field:Schema(description = "감정 태그 목록 (현재는 최대 1개, 확장 가능)", example = "[\"감동적\"]")
    val emotionTags: List<@Size(max = 10, message = "감정 태그는 10자를 초과할 수 없습니다.") String> = emptyList()
) {
    fun validPageNumber(): Int = pageNumber!!
    fun validQuote(): String = quote!!
    
    fun validEmotionTags(): List<String> = emotionTags
}
