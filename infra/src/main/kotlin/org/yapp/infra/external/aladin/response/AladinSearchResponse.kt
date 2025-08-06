package org.yapp.infra.external.aladin.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

@JsonIgnoreProperties(ignoreUnknown = true)
data class AladinSearchResponse(
    @JsonProperty("version") val version: String?,
    @JsonProperty("title") val title: String?,
    @JsonProperty("link") val link: String?,
    @JsonProperty("pubDate") val pubDate: String?,
    @JsonProperty("totalResults") val totalResults: Int?,
    @JsonProperty("startIndex") val startIndex: Int?,
    @JsonProperty("itemsPerPage") val itemsPerPage: Int?,
    @JsonProperty("query") val query: String? = null,
    @JsonProperty("searchCategoryId") val searchCategoryId: Int? = null,
    @JsonProperty("searchCategoryName") val searchCategoryName: String? = null,
    @JsonProperty("item") val item: List<AladinSearchItem> = emptyList()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class AladinSearchItem(
    @JsonProperty("title") val title: String,
    @JsonProperty("link") val link: String,
    @JsonProperty("author") val author: String?,
    @JsonProperty("pubDate") val pubDate: String?,
    @JsonProperty("description") val description: String?,
    @JsonProperty("isbn") val isbn: String?,
    @JsonProperty("isbn13") val isbn13: String?,
    @JsonProperty("itemId") val itemId: Long,
    @JsonProperty("priceSales") val priceSales: BigDecimal,
    @JsonProperty("priceStandard") val priceStandard: BigDecimal,
    @JsonProperty("mallType") val mallType: String,
    @JsonProperty("stockStatus") val stockStatus: String?,
    @JsonProperty("mileage") val mileage: Int?,
    @JsonProperty("cover") val cover: String,
    @JsonProperty("categoryId") val categoryId: Int,
    @JsonProperty("categoryName") val categoryName: String,
    @JsonProperty("publisher") val publisher: String?
)
