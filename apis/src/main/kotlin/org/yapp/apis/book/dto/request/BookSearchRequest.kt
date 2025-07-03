package org.yapp.apis.book.dto.request

import jakarta.validation.constraints.NotBlank

/**
 * 도서 검색 요청을 위한 DTO.
 * 클라이언트로부터 검색 관련 파라미터를 받습니다.
 */
data class BookSearchRequest private constructor(
    @field:NotBlank(message = "검색어는 필수입니다.")
    val query: String,
    val queryType: String? = null,
    val searchTarget: String? = null,
    val maxResults: Int? = null,
    val start: Int? = null,
    val sort: String? = null,
    val cover: String? = null,
    val categoryId: Int? = null
) {
    /**
     * 알라딘 API 호출을 위한 쿼리 파라미터 맵으로 변환합니다.
     * @return 알라딘 API 쿼리 파라미터 맵.
     */
    fun toAladinParams(): Map<String, Any> {
        val params = mutableMapOf<String, Any>()
        queryType?.let { params["QueryType"] = it }
        searchTarget?.let { params["SearchTarget"] = it }
        maxResults?.let { params["MaxResults"] = it }
        start?.let { params["Start"] = it } // 알라딘 API는 'Start' 파라미터 이름을 사용
        sort?.let { params["Sort"] = it }
        cover?.let { params["Cover"] = it }
        categoryId?.let { params["CategoryId"] = it }
        return params
    }
}
