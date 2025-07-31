package org.yapp.apis.book.dto.response

import org.yapp.domain.userbook.BookStatus
import org.yapp.domain.userbook.UserBook
import org.yapp.infra.external.aladin.response.AladinSearchResponse
import org.yapp.infra.external.aladin.response.BookItem
import java.time.LocalDateTime

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
    fun from(updatedBooks: List<BookSummary>): BookSearchResponse {
        return BookSearchResponse(
            version = this.version,
            title = this.title,
            link = this.link,
            pubDate = this.pubDate,
            totalResults = this.totalResults,
            startIndex = this.startIndex,
            itemsPerPage = this.itemsPerPage,
            query = this.query,
            searchCategoryId = this.searchCategoryId,
            searchCategoryName = this.searchCategoryName,
            books = updatedBooks
        )
    }

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
        val userBookStatus: BookStatus
    ) {
        fun updateStatus(newStatus: BookStatus): BookSummary {
            return this.copy(userBookStatus = newStatus)
        }

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
                    userBookStatus = BookStatus.BEFORE_REGISTRATION
                )
            }
        }
    }
}
