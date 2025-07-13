package org.yapp.apis.book.dto.response

import org.yapp.domain.userbook.BookStatus
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
    val books: List<BookSummary>
) {
    companion object {
        fun from(response: AladinSearchResponse): BookSearchResponse {
            val books = response.item?.mapNotNull { BookSummary.fromAladinItem(it) } ?: emptyList()
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

    data class BookSummary private constructor(
        val isbn: String,
        val title: String,
        val author: String?,
        val publisher: String?,
        val coverImageUrl: String?,
        var userBookStatus: BookStatus
    ) {
        companion object {
            private val unknownTitle = "제목없음"

            fun fromAladinItem(item: BookItem): BookSummary? {
                val isbn = item.isbn ?: item.isbn13 ?: return null
                return BookSummary(
                    isbn = isbn,
                    title = item.title ?: unknownTitle,
                    author = item.author,
                    publisher = item.publisher,
                    coverImageUrl = item.cover,
                    userBookStatus = BookStatus.BEFORE_READING
                )
            }
        }
    }
}
