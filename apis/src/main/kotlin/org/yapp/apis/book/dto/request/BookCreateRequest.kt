package org.yapp.apis.book.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.yapp.apis.book.dto.response.BookDetailResponse

@Schema(
    title = "책 생성 요청",
    description = "시스템에 새로운 책 정보를 생성하는 요청 (주로 내부 API에서 사용)"
)
data class BookCreateRequest private constructor(
    @field:NotBlank(message = "ISBN은 필수입니다.")
    @Schema(
        description = "책의 13자리 ISBN 코드",
        example = "9788932473901",
        required = true,
        minLength = 13,
        maxLength = 13
    )
    val isbn: String? = null,

    @field:NotBlank(message = "제목은 필수입니다.")
    @field:Size(max = 500, message = "제목은 500자 이내여야 합니다.")
    @Schema(
        description = "책 제목",
        example = "해리 포터와 마법사의 돌",
        required = true,
        maxLength = 500
    )
    val title: String? = null,

    @field:NotBlank(message = "저자는 필수입니다.")
    @field:Size(max = 200, message = "저자는 200자 이내여야 합니다.")
    @Schema(
        description = "저자명 (여러 저자인 경우 쉼표로 구분)",
        example = "J.K. 롤링",
        required = true,
        maxLength = 200
    )
    val author: String? = null,

    @field:NotBlank(message = "출판사는 필수입니다.")
    @field:Size(max = 200, message = "출판사는 200자 이내여야 합니다.")
    @Schema(
        description = "출판사명",
        example = "문학수첩",
        required = true,
        maxLength = 200
    )
    val publisher: String? = null,

    @field:Min(value = 1000, message = "출간연도는 1000년 이후여야 합니다.")
    @field:Max(value = 2100, message = "출간연도는 2100년 이전이어야 합니다.")
    @Schema(
        description = "출간연도 (4자리 년도)",
        example = "2000",
        minimum = "1000",
        maximum = "2100"
    )
    val publicationYear: Int? = null,

    @field:Size(max = 2048, message = "표지 URL은 2048자 이내여야 합니다.")
    @Schema(
        description = "책 표지 이미지 URL",
        example = "https://image.aladin.co.kr/product/123/45/cover/1234567890123.jpg",
        required = true,
        maxLength = 2048,
        format = "uri"
    )
    val coverImageUrl: String,

    @field:Size(max = 2000, message = "책 설명은 2000자 이내여야 합니다.")
    @Schema(
        description = "책 소개 및 줄거리",
        example = "11살 해리 포터는 이모네 집에서 갖은 구박을 당하며 지낸다...",
        maxLength = 2000
    )
    val description: String? = null
) {
    fun validIsbn(): String = isbn!!
    fun validTitle(): String = title!!
    fun validAuthor(): String = author!!
    fun validPublisher(): String = publisher!!

    companion object {

        fun from(bookDetail: BookDetailResponse): BookCreateRequest {
            val finalIsbn = bookDetail.isbn13
            ?: throw IllegalArgumentException("ISBN이 존재하지 않습니다.")

            return BookCreateRequest(
                isbn = finalIsbn,
                title = bookDetail.title,
                author = bookDetail.author,
                publisher = bookDetail.publisher,
                publicationYear = parsePublicationYear(bookDetail.pubDate),
                coverImageUrl = bookDetail.coverImageUrl,
                description = bookDetail.description,
            )
        }

        private fun parsePublicationYear(pubDate: String?): Int? {
            return pubDate
                ?.takeIf { it.length >= 4 && it.substring(0, 4).all { ch -> ch.isDigit() } }
                ?.substring(0, 4)
                ?.toIntOrNull()
        }
    }
}
