package org.yapp.apis.book.dto.response

import org.yapp.infra.external.aladin.response.AladinSearchResponse
import org.yapp.infra.external.aladin.response.BookItem

data class BookSearchResponse private constructor(
    val version: String?,
    val title: String?,
    val link: String?,
    val pubDate: String?,
    val totalResults: Int?,
    val startIndex: Int?,
    val itemsPerPage: Int?,
    val query: String?,
    val searchCategoryId: Int?,
    val searchCategoryName: String?,
    val books: List<BookSummaryDto>
) {


    companion object {
        fun from(response: AladinSearchResponse): BookSearchResponse {
            val books = response.item?.mapNotNull { BookSummaryDto.fromAladinItem(it) } ?: emptyList()
            return BookSearchResponse(
                version = response.version,
                title = response.title,
                link = response.link,
                pubDate = response.pubDate,
                totalResults = response.totalResults,
                startIndex = response.startIndex,
                itemsPerPage = response.itemsPerPage,
                query = response.query,
                searchCategoryId = response.searchCategoryId,
                searchCategoryName = response.searchCategoryName,
                books = books
            )
        }
    }

    data class BookSummaryDto private constructor(
        val isbn: String,
        val title: String,
        val author: String?,
        val publisher: String?,
        val coverImageUrl: String?,
    ) {

        companion object {

            private val unknownTitle = "제목없음"

            fun fromAladinItem(item: BookItem): BookSummaryDto? {
                val isbn = item.isbn ?: item.isbn13 ?: return null
                return BookSummaryDto(
                    isbn = isbn,
                    title = item.title ?: unknownTitle,
                    author = item.author,
                    publisher = item.publisher,
                    coverImageUrl = item.cover
                )
            }
        }
    }
}
