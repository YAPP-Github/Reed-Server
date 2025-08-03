package org.yapp.infra.external.aladin.dto

data class AladinBookSearchRequest private constructor(
    val query: String,
    val queryType: String?,
    val searchTarget: String?,
    val maxResults: Int?,
    val start: Int?,
    val sort: String?,
    val cover: String?,
    val categoryId: Int?
) {
    fun toMap(): Map<String, Any> {
        val params = mutableMapOf<String, Any>()
        params["Query"] = query
        queryType?.let { params["QueryType"] = it }
        searchTarget?.let { params["SearchTarget"] = it }
        maxResults?.let { params["MaxResults"] = it }
        start?.let { params["Start"] = it }
        sort?.let { params["Sort"] = it }
        cover?.let { params["Cover"] = it }
        categoryId?.let { params["CategoryId"] = it }
        return params
    }

    companion object {
        fun of(
            query: String,
            queryType: String? = null,
            searchTarget: String? = null,
            maxResults: Int? = null,
            start: Int? = null,
            sort: String? = null,
            categoryId: Int? = null
        ): AladinBookSearchRequest {
            return AladinBookSearchRequest(
                query = query,
                queryType = queryType,
                searchTarget = searchTarget,
                maxResults = maxResults,
                start = start,
                sort = sort,
                cover = "Big",
                categoryId = categoryId
            )
        }
    }
}
