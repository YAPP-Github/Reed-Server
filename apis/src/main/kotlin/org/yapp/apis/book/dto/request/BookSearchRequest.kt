package org.yapp.apis.book.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

@Schema(
    title = "책 검색 요청",
    description = "알라딘 API를 통한 책 검색 요청 정보"
)
data class BookSearchRequest private constructor(
    @field:NotBlank(message = "검색어는 필수입니다.")
    @Schema(
        description = "검색할 키워드 (제목, 저자, 출판사 등)",
        example = "해리포터",
        required = true
    )
    val query: String? = null,

    @Schema(
        description = "검색 유형",
        example = "Title",
        allowableValues = ["Title", "Author", "Publisher", "Keyword"],
        defaultValue = "Keyword"
    )
    val queryType: String? = null,

    @Schema(
        description = "검색 대상",
        example = "Book",
        allowableValues = ["Book", "Foreign", "Music", "DVD"],
        defaultValue = "Book"
    )
    val searchTarget: String? = null,

    @field:Min(value = 1, message = "최대 결과 수는 1 이상이어야 합니다.")
    @field:Max(value = 100, message = "최대 결과 수는 100 이하여야 합니다.")
    @Schema(
        description = "한 번에 가져올 최대 결과 수 (1-100)",
        example = "10",
        minimum = "1",
        maximum = "100",
        defaultValue = "10"
    )
    val maxResults: Int? = null,

    @field:Min(value = 1, message = "시작 인덱스는 1 이상이어야 합니다.")
    @Schema(
        description = "검색 시작 인덱스 (페이징)",
        example = "1",
        minimum = "1",
        defaultValue = "1"
    )
    val start: Int? = null,

    @Schema(
        description = "정렬 방식",
        example = "Accuracy",
        allowableValues = ["Accuracy", "PublishTime", "Title", "SalesPoint"],
        defaultValue = "Accuracy"
    )
    val sort: String? = null,

    @Schema(
        description = "카테고리 ID (0: 전체)",
        example = "0",
        defaultValue = "0"
    )
    val categoryId: Int? = null
) {
    fun validQuery(): String = query!!
}
