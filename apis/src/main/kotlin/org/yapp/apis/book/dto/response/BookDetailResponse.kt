package org.yapp.apis.book.dto.response

import org.yapp.infra.external.aladin.response.AladinBookDetailResponse
import java.math.BigDecimal

data class BookDetailResponse private constructor(
    val version: String?,
    val title: String,
    val link: String?,
    val author: String,
    val pubDate: String?,
    val description: String?,
    val isbn: String?,
    val isbn13: String?,
    val itemId: Long?,
    val priceSales: BigDecimal?,
    val priceStandard: BigDecimal?,
    val mallType: String?,
    val stockStatus: String?,
    val mileage: Int?,
    val cover: String,
    val categoryId: Int?,
    val categoryName: String?,
    val publisher: String,
) {
    companion object {
        fun from(response: AladinBookDetailResponse): BookDetailResponse {
            val bookItem = response.item?.firstOrNull()
                ?: throw IllegalArgumentException("No book item found in detail response.")

            return BookDetailResponse(
                version = response.version,
                title = bookItem.title ?: "",
                link = bookItem.link,
                author = bookItem.author ?: "",
                pubDate = bookItem.pubDate,
                description = bookItem.description ?: "",
                isbn = bookItem.isbn ?: bookItem.isbn13 ?: "",
                isbn13 = bookItem.isbn13,
                itemId = bookItem.itemId,
                priceSales = bookItem.priceSales,
                priceStandard = bookItem.priceStandard,
                mallType = bookItem.mallType,
                stockStatus = bookItem.stockStatus,
                mileage = bookItem.mileage,
                cover = bookItem.cover ?: "",
                categoryId = bookItem.categoryId,
                categoryName = bookItem.categoryName,
                publisher = bookItem.publisher ?: "",
            )
        }
    }
}
