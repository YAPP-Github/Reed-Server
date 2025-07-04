package org.yapp.apis.book.dto.request

import jakarta.validation.constraints.NotBlank
import org.yapp.infra.external.aladin.dto.AladinBookSearchRequest


data class BookSearchRequest private constructor(
    @field:NotBlank(message = "검색어는 필수입니다.")
    val query: String? = null,
    val queryType: String? = null,
    val searchTarget: String? = null,
    val maxResults: Int? = null,
    val start: Int? = null,
    val sort: String? = null,
    val cover: String? = null,
    val categoryId: Int? = null
) {

    fun toAladinRequest(): AladinBookSearchRequest {
        require(!query.isNullOrBlank()) { "검색어(query)는 필수입니다." }

        return AladinBookSearchRequest.create(
            query = this.query,
            queryType = this.queryType,
            searchTarget = this.searchTarget,
            maxResults = this.maxResults,
            start = this.start,
            sort = this.sort,
            cover = this.cover,
            categoryId = this.categoryId
        )
    }
}
