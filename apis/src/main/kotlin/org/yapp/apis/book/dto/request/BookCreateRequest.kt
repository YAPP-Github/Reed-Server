package org.yapp.apis.book.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*
import org.yapp.apis.book.dto.response.BookDetailResponse
import org.yapp.globalutils.util.RegexUtils

@Schema(
    name = "BookCreateRequest",
    description = "시스템에 새로운 책 정보를 생성하는 요청 (주로 내부 API에서 사용)"
)
data class BookCreateRequest private constructor(
    @field:NotBlank(message = "ISBN13은 필수입니다.")
    @field:Pattern(
        regexp = RegexUtils.ISBN13_PATTERN,
        message = "유효한 13자리 ISBN13 형식이 아닙니다."
    )
    @field:Schema(
        description = "책의 13자리 ISBN13 코드",
        example = "9788932473901",
        required = true,
        minLength = 13,
        maxLength = 13
    )
    val isbn13: String? = null,

    @field:NotBlank(message = "제목은 필수입니다.")
    @field:Size(max = 500, message = "제목은 500자 이내여야 합니다.")
    @field:Schema(
        description = "책 제목",
        example = "해리 포터와 마법사의 돌",
        required = true,
        maxLength = 500
    )
    val title: String? = null,

    @field:NotBlank(message = "저자는 필수입니다.")
    @field:Size(max = 200, message = "저자는 200자 이내여야 합니다.")
    @field:Schema(
        description = "저자명 (여러 저자인 경우 쉼표로 구분)",
        example = "J.K. 롤링",
        required = true,
        maxLength = 200
    )
    val author: String? = null,

    @field:NotBlank(message = "출판사는 필수입니다.")
    @field:Size(max = 200, message = "출판사는 200자 이내여야 합니다.")
    @field:Schema(
        description = "출판사명",
        example = "문학수첩",
        required = true,
        maxLength = 200
    )
    val publisher: String? = null,

    @field:Min(value = 1000, message = "출간연도는 1000년 이후여야 합니다.")
    @field:Max(value = 2100, message = "출간연도는 2100년 이전이어야 합니다.")
    @field:Schema(
        description = "출간연도 (4자리 년도)",
        example = "2000",
        minimum = "1000",
        maximum = "2100"
    )
    val publicationYear: Int? = null,

    @field:Size(max = 2048, message = "표지 URL은 2048자 이내여야 합니다.")
    @field:NotBlank(message = "표지 이미지 URL은 필수입니다.")
    @field:Schema(
        description = "책 표지 이미지 URL",
        example = "https://image.aladin.co.kr/product/123/45/cover/1234567890123.jpg",
        required = true,
        maxLength = 2048,
        format = "uri"
    )
    val coverImageUrl: String? = null,

    @field:Size(max = 2000, message = "책 설명은 2000자 이내여야 합니다.")
    @field:Schema(
        description = "책 소개 및 줄거리",
        example = "11살 해리 포터는 이모네 집에서 갖은 구박을 당하며 지낸다...",
        maxLength = 2000
    )
    val description: String? = null
) {
    fun validIsbn13(): String = isbn13!!
    fun validTitle(): String = title!!
    fun validAuthor(): String = author!!
    fun validPublisher(): String = publisher!!
    fun validCoverImageUrl(): String = coverImageUrl!!

    companion object {
        private const val UNKNOWN_AUTHOR = "저자 정보 없음"
        private const val UNKNOWN_PUBLISHER = "출판사 정보 없음"
        private const val DEFAULT_COVER_IMAGE = "https://github.com/user-attachments/assets/7ba556a4-3a76-4f27-aecb-e58924e66843"

        fun from(bookDetailResponse: BookDetailResponse): BookCreateRequest {
            val finalIsbn13 = bookDetailResponse.isbn13
                ?: throw IllegalArgumentException("ISBN13이 존재하지 않습니다.")

            return BookCreateRequest(
                isbn13 = finalIsbn13,
                author = provideDefaultIfBlank(bookDetailResponse.author, UNKNOWN_AUTHOR),
                publisher = provideDefaultIfBlank(bookDetailResponse.publisher, UNKNOWN_PUBLISHER),
                publicationYear = parsePublicationYear(bookDetailResponse.pubDate),
                coverImageUrl = provideDefaultIfBlank(bookDetailResponse.coverImageUrl, DEFAULT_COVER_IMAGE),
                description = bookDetailResponse.description
            )
        }

        private fun provideDefaultIfBlank(input: String?, defaultValue: String): String {
            return if (input.isNullOrBlank()) defaultValue else input
        }

        private fun parsePublicationYear(pubDate: String?): Int? {
            return pubDate
                ?.takeIf { it.length >= 4 && it.substring(0, 4).all { ch -> ch.isDigit() } }
                ?.substring(0, 4)
                ?.toIntOrNull()
        }
    }
}
