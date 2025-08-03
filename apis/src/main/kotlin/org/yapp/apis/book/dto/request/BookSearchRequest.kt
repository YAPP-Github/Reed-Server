package org.yapp.apis.book.dto.request

import org.yapp.infra.external.aladin.dto.AladinBookSearchRequest

data class BookSearchRequest private constructor(
    val query: String? = null,
    val queryType: String? = null,
    val searchTarget: String? = null,
    val maxResults: Int? = null,
    val start: Int? = null,
    val sort: String? = null,
    val categoryId: Int? = null
) {
    fun validQuery(): String = query!!

    fun toAladinRequest(): AladinBookSearchRequest {
        return AladinBookSearchRequest.of(
            query = this.validQuery(),
            queryType = this.queryType,
            searchTarget = this.searchTarget,
            maxResults = this.maxResults,
            start = this.start,
            sort = this.sort,
            categoryId = this.categoryId
        )
    }
}
