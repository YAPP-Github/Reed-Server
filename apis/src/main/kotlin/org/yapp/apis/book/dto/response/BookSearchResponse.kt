package org.yapp.apis.book.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.yapp.apis.book.util.AuthorExtractor
import org.yapp.apis.book.util.IsbnConverter
import org.yapp.domain.userbook.BookStatus
import org.yapp.infra.external.aladin.response.AladinSearchResponse

@Schema(
    name = "BookSearchResponse",
    description = "알라딘 도서 검색 API 응답"
)
data class BookSearchResponse private constructor(

    @Schema(description = "API 응답 버전", example = "20131101")
    val version: String?,

    @Schema(description = "검색 결과 제목", example = "데미안")
    val title: String?,

    @Schema(description = "출간일", example = "2025-07-30")
    val pubDate: String?,

    @Schema(description = "총 검색 결과 개수", example = "42")
    val totalResults: Int?,

    @Schema(description = "검색 시작 인덱스", example = "1")
    val startIndex: Int?,

    @Schema(description = "한 페이지당 검색 결과 개수", example = "10")
    val itemsPerPage: Int?,

    @Schema(description = "검색 쿼리 문자열", example = "데미안")
    val query: String?,

    @Schema(description = "검색 카테고리 ID", example = "1")
    val searchCategoryId: Int?,

    @Schema(description = "검색 카테고리 이름", example = "소설/시/희곡")
    val searchCategoryName: String?,

    @Schema(description = "검색된 책 목록")
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
                        link = it.link,
                        author = AuthorExtractor.extractAuthors(it.author),
                        publisher = it.publisher,
                        coverImageUrl = it.cover
                    )
                }
            )
        }
    }

    @Schema(name = "BookSummary", description = "검색된 단일 책 요약 정보")
    data class BookSummary private constructor(

        @Schema(description = "ISBN-13 번호", example = "9781234567890")
        val isbn13: String,

        @Schema(description = "책 제목", example = "데미안")
        val title: String,

        @Schema(
            description = "검색 결과 상세 페이지 링크",
            example = "https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=367872613&amp;partner=openAPI&amp;start=api"
        )
        val link: String?,

        @Schema(description = "저자", example = "헤르만 헤세")
        val author: String?,

        @Schema(description = "출판사", example = "민음사")
        val publisher: String?,

        @Schema(
            description = "책 표지 이미지 URL",
            example = "https://image.aladin.co.kr/product/36801/75/coversum/k692030806_1.jpg"
        )
        val coverImageUrl: String,

        @Schema(description = "사용자의 책 상태", example = "BEFORE_REGISTRATION")
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
                link: String?,
                author: String?,
                publisher: String?,
                coverImageUrl: String
            ): BookSummary {
                require(!title.isNullOrBlank()) { "Title is required" }

                return BookSummary(
                    isbn13 = isbn13 ?: IsbnConverter.toIsbn13(isbn)
                    ?: throw IllegalArgumentException("Either isbn13 or isbn must be provided"),
                    title = title,
                    link = link,
                    author = author,
                    publisher = publisher,
                    coverImageUrl = coverImageUrl,
                    userBookStatus = BookStatus.BEFORE_REGISTRATION
                )
            }
        }
    }
}
