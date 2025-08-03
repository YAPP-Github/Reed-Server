package org.yapp.apis.book.dto.request

import jakarta.validation.constraints.NotBlank

data class BookSearchRequest private constructor(
    @field:NotBlank(message = "검색어는 필수입니다.")
    val query: String? = null,
    val queryType: String? = null,
    val searchTarget: String? = null,
    val maxResults: Int? = null,
    val start: Int? = null,
    val sort: String? = null,
    val categoryId: Int? = null
) {
    fun validQuery(): String = query!!
}
