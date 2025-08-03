package org.yapp.apis.book.dto.response

import org.yapp.domain.userbook.BookStatus
import org.yapp.apis.util.IsbnConverter
import org.yapp.infra.external.aladin.response.AladinSearchResponse

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
    fun withUpdatedBooks(updatedBooks: List<BookSummary>): BookSearchResponse {
        return this.copy(books = updatedBooks)
    }

    companion object {
        fun from(response: AladinSearchResponse): BookSearchResponse {
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
                books = response.item.map {
                    BookSummary.of(
                        isbn = it.isbn,
                        isbn13 = it.isbn13,
                        title = it.title,
                        author = it.author,
                        publisher = it.publisher,
                        coverImageUrl = it.cover
                    )
                }
            )
        }
    }

    data class BookSummary private constructor(
        val isbn13: String,
        val title: String,
        val author: String?,
        val publisher: String?,
        val coverImageUrl: String,
        val userBookStatus: BookStatus
    ) {
        fun updateStatus(newStatus: BookStatus): BookSummary {
            return this.copy(userBookStatus = newStatus)
        }

        companion object {
            fun of(
                isbn: String?,
                isbn13: String?,
                title: String?,
                author: String?,
                publisher: String?,
                coverImageUrl: String
            ): BookSummary {
                require(!title.isNullOrBlank()) { "Title is required" }

                return BookSummary(
                    isbn13 = isbn13 ?: IsbnConverter.toIsbn13(isbn) ?: throw IllegalArgumentException("Either isbn13 or isbn must be provided"),
                    title = title,
                    author = author,
                    publisher = publisher,
                    coverImageUrl = coverImageUrl,
                    userBookStatus = BookStatus.BEFORE_REGISTRATION
                )
            }
        }
    }
}
