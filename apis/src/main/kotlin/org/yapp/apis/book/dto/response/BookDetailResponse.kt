package org.yapp.apis.book.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.yapp.apis.book.util.AuthorExtractor
import org.yapp.apis.book.util.IsbnConverter
import org.yapp.domain.userbook.BookStatus
import org.yapp.globalutils.validator.BookDataValidator
import org.yapp.infra.external.aladin.response.AladinBookDetailResponse

@Schema(
    name = "BookDetailResponse",
    description = "책 상세 정보"
)
data class BookDetailResponse private constructor(

    @field:Schema(description = "알라딘 API 응답 버전", example = "20131101")
    val version: String?,

    @field:Schema(description = "책 제목", example = "데미안")
    val title: String,

    @field:Schema(
        description = "검색 결과 상세 페이지 링크",
        example = "https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=368017530&amp;partner=openAPI"
    )
    val link: String,

    @field:Schema(description = "저자", example = "헤르만 헤세")
    val author: String?,

    @field:Schema(description = "출간일", example = "2025-07-30")
    val pubDate: String,

    @field:Schema(description = "책 설명", example = "인간의 성장과 자아 탐구를 다룬 소설")
    val description: String,

    @field:Schema(description = "ISBN-13 번호", example = "9791164053353")
    val isbn13: String?,

    @field:Schema(description = "쇼핑몰 타입", example = "BOOK")
    val mallType: String,

    @field:Schema(
        description = "책 표지 이미지 URL",
        example = "https://image.aladin.co.kr/product/36801/75/coversum/k692030806_1.jpg"
    )
    val coverImageUrl: String,

    @field:Schema(description = "카테고리명", example = "국내도서>소설/시/희곡>독일소설")
    val categoryName: String,

    @field:Schema(description = "출판사", example = "북하우스")
    val publisher: String?,

    @field:Schema(description = "총 페이지 수", example = "344")
    val totalPage: Int?,

    @field:Schema(description = "사용자의 책 상태", example = "BEFORE_REGISTRATION")
    val userBookStatus: BookStatus
) {
    fun withUserBookStatus(newUserBookStatus: BookStatus): BookDetailResponse {
        return this.copy(userBookStatus = newUserBookStatus)
    }

    companion object {
        private const val DEFAULT_MAX_PAGE_COUNT = 4032

        fun from(
            response: AladinBookDetailResponse,
            userBookStatus: BookStatus = BookStatus.BEFORE_REGISTRATION
        ): BookDetailResponse {
            val item = response.item.firstOrNull()
                ?: throw IllegalArgumentException("No book item found in detail response.")

            val isbn13 = item.isbn13?.takeIf { it.isNotBlank() }
                ?: IsbnConverter.toIsbn13(item.isbn?.takeIf { it.isNotBlank() })
                ?: throw IllegalArgumentException("Either isbn13 or isbn must be provided")

            return BookDetailResponse(
                version = response.version,
                title = item.title,
                link = item.link,
                author = AuthorExtractor.extractAuthors(item.author),
                pubDate = item.pubDate ?: "",
                description = item.description ?: "",
                mallType = item.mallType,
                isbn13 = isbn13,
                coverImageUrl = item.cover,
                categoryName = item.categoryName,
                publisher = item.publisher?.let { BookDataValidator.removeParenthesesFromPublisher(it) } ?: "",
                totalPage = item.subInfo.itemPage ?: DEFAULT_MAX_PAGE_COUNT,
                userBookStatus = userBookStatus
            )
        }
    }
}
