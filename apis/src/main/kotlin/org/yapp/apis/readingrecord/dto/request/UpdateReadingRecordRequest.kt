package org.yapp.apis.readingrecord.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

@Schema(
    name = "UpdateReadingRecordRequest",
    description = "독서 기록 수정 요청",
    example = """
        {
          "pageNumber": 50,
          "quote": "수정된 기억에 남는 문장입니다.",
          "review": "수정된 감상평입니다.",
          "emotionTags": ["놀라움"]
        }
    """
)
data class UpdateReadingRecordRequest private constructor(
    @field:Min(1, message = "페이지 번호는 1 이상이어야 합니다.")
    @field:Max(9999, message = "페이지 번호는 9999 이하여야 합니다.")
    @field:Schema(description = "수정할 페이지 번호", example = "50")
    val pageNumber: Int?,

    @field:Size(max = 1000, message = "기억에 남는 문장은 1000자를 초과할 수 없습니다.")
    @field:Schema(description = "수정할 기억에 남는 문장", example = "수정된 기억에 남는 문장입니다.")
    val quote: String?,

    @field:Size(max = 1000, message = "감상평은 1000자를 초과할 수 없습니다.")
    @field:Schema(description = "수정할 감상평", example = "수정된 감상평입니다.")
    val review: String?,

    @field:Size(max = 1, message = "감정 태그는 최대 1개까지 가능합니다.")
    @field:Schema(description = "수정할 감정 태그 목록", example = """["놀라움"]""")
    val emotionTags: List<@Size(max = 10, message = "감정 태그는 10자를 초과할 수 없습니다.") String>?
) {
    fun validPageNumber(): Int? = pageNumber
    fun validQuote(): String? = quote
    fun validReview(): String? = review
    fun validEmotionTags(): List<String>? = emotionTags
}
