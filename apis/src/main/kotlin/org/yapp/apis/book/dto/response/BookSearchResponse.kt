import org.yapp.domain.book.Book
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
            fun fromAladinItem(item: BookItem): BookSummaryDto? {
                val isbn = item.isbn ?: item.isbn13 ?: return null
                val book = Book.create(
                    title = item.title ?: "제목 없음",
                    author = item.author,
                    publisher = item.publisher,
                    publicationYear = item.pubDate?.substringBefore("-")?.toIntOrNull(),
                    coverImageUrl = item.cover,
                    description = item.description,
                    isbn = isbn
                )
                return from(book)
            }

            fun from(book: Book): BookSummaryDto {
                return BookSummaryDto(
                    isbn = book.isbn,
                    title = book.title,
                    author = book.author,
                    publisher = book.publisher,
                    coverImageUrl = book.coverImageUrl
                )
            }
        }
    }
}
