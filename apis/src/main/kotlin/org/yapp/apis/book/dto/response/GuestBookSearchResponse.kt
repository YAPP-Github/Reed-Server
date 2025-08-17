package org.yapp.apis.book.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.yapp.globalutils.validator.BookDataValidator

@Schema(
    name = "GuestBookSearchResponse",
    description = "게스트용 알라딘 도서 검색 API 응답"
)
data class GuestBookSearchResponse private constructor(
    @field:Schema(description = "API 응답 버전", example = "20131101")
    val version: String?,

    @field:Schema(description = "검색 결과 제목", example = "데미안")
    val title: String?,

    @field:Schema(description = "출간일", example = "2025-07-30")
    val pubDate: String?,

    @field:Schema(description = "총 검색 결과 개수", example = "42")
    val totalResults: Int?,

    @field:Schema(description = "검색 시작 인덱스", example = "1")
    val startIndex: Int?,

    @field:Schema(description = "한 페이지당 검색 결과 개수", example = "10")
    val itemsPerPage: Int?,

    @field:Schema(description = "검색 쿼리 문자열", example = "데미안")
    val query: String?,

    @field:Schema(description = "검색 카테고리 ID", example = "1")
    val searchCategoryId: Int?,

    @field:Schema(description = "검색 카테고리 이름", example = "소설/시/희곡")
    val searchCategoryName: String?,

    @field:Schema(description = "마지막 페이지 여부", example = "false")
    val lastPage: Boolean,

    @field:Schema(description = "검색된 책 목록 (게스트용)")
    val books: List<GuestBookSummary>
) {
    companion object {
        fun from(response: BookSearchResponse): GuestBookSearchResponse {
            return GuestBookSearchResponse(
                version = response.version,
                title = response.title,
                pubDate = response.pubDate,
                totalResults = response.totalResults,
                startIndex = response.startIndex,
                itemsPerPage = response.itemsPerPage,
                query = response.query,
                searchCategoryId = response.searchCategoryId,
                searchCategoryName = response.searchCategoryName,
                lastPage = response.lastPage,
                books = response.books.map { userBookSummary ->
                    GuestBookSummary.of(
                        isbn13 = userBookSummary.isbn13,
                        title = userBookSummary.title,
                        author = userBookSummary.author,
                        publisher = userBookSummary.publisher,
                        coverImageUrl = userBookSummary.coverImageUrl,
                        link = userBookSummary.link
                    )
                }
            )
        }
    }

    @Schema(name = "GuestBookSummary", description = "게스트용 검색된 단일 책 요약 정보")
    data class GuestBookSummary private constructor(

        @field:Schema(description = "ISBN-13 번호", example = "9781234567890")
        val isbn13: String,

        @field:Schema(description = "책 제목", example = "데미안")
        val title: String,

        @field:Schema(description = "저자", example = "헤르만 헤세")
        val author: String?,

        @field:Schema(description = "출판사", example = "민음사")
        val publisher: String?,

        @field:Schema(
            description = "책 표지 이미지 URL",
            example = "https://image.aladin.co.kr/product/36801/75/coversum/k692030806_1.jpg"
        )
        val coverImageUrl: String,

        @field:Schema(
            description = "알라딘 도서 상세 페이지 링크",
            example = "http://www.aladin.co.kr/shop/wproduct.aspx?ItemId=3680175"
        )
        val link: String
    ) {
        companion object {
            fun of(
                isbn13: String,
                title: String,
                author: String?,
                publisher: String?,
                coverImageUrl: String,
                link: String
            ): GuestBookSummary {
                return GuestBookSummary(
                    isbn13 = isbn13,
                    title = title,
                    author = author,
                    publisher = publisher?.let { BookDataValidator.removeParenthesesFromPublisher(it) },
                    coverImageUrl = coverImageUrl,
                    link = link
                )
            }
        }
    }
}
