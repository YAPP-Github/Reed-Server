package org.yapp.apis.book.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.yapp.apis.book.dto.response.BookCreateResponse
import org.yapp.domain.userbook.BookStatus
import java.util.UUID

@Schema(
    title = "사용자 도서 생성/수정 요청",
    description = "사용자 서재에 도서를 생성하거나 기존 도서 정보를 수정하는 내부 API 요청 (주로 내부 서비스에서 사용)"
)
data class UpsertUserBookRequest private constructor(
    @field:NotNull(message = "사용자 ID는 필수입니다.")
    @Schema(
        description = "사용자 고유 식별자",
        example = "550e8400-e29b-41d4-a716-446655440000",
        required = true,
        format = "uuid"
    )
    val userId: UUID? = null,

    @field:NotNull(message = "책 ID는 필수입니다.")
    @Schema(
        description = "책 고유 식별자",
        example = "550e8400-e29b-41d4-a716-446655440001",
        required = true,
        format = "uuid"
    )
    val bookId: UUID? = null,

    @field:NotBlank(message = "책 ISBN은 필수입니다.")
    @Schema(
        description = "책의 13자리 ISBN 코드",
        example = "9788932473901",
        required = true,
        minLength = 13,
        maxLength = 13
    )
    val bookIsbn: String? = null,

    @field:NotBlank(message = "책 제목은 필수입니다.")
    @field:Size(max = 500, message = "책 제목은 500자 이내여야 합니다.")
    @Schema(
        description = "책 제목",
        example = "해리 포터와 마법사의 돌",
        required = true,
        maxLength = 500
    )
    val bookTitle: String? = null,

    @field:NotBlank(message = "저자는 필수입니다.")
    @field:Size(max = 200, message = "저자는 200자 이내여야 합니다.")
    @Schema(
        description = "저자명",
        example = "J.K. 롤링",
        required = true,
        maxLength = 200
    )
    val bookAuthor: String? = null,

    @field:NotBlank(message = "출판사는 필수입니다.")
    @field:Size(max = 200, message = "출판사는 200자 이내여야 합니다.")
    @Schema(
        description = "출판사명",
        example = "문학수첩",
        required = true,
        maxLength = 200
    )
    val bookPublisher: String? = null,

    @field:NotBlank(message = "표지 이미지 URL은 필수입니다.")
    @field:Size(max = 2048, message = "표지 이미지 URL은 2048자 이내여야 합니다.")
    @Schema(
        description = "책 표지 이미지 URL",
        example = "https://image.aladin.co.kr/product/123/45/cover/1234567890123.jpg",
        required = true,
        maxLength = 2048,
        format = "uri"
    )
    val bookCoverImageUrl: String? = null,

    @field:NotNull(message = "도서 상태는 필수입니다.")
    @Schema(
        description = "사용자의 도서 읽기 상태",
        example = "READING",
        required = true,
        allowableValues = ["BEFORE_REGISTRATION", "BEFORE_READING", "READING", "COMPLETED"],
        enumAsRef = true
    )
    val status: BookStatus? = null
) {
    fun validUserId(): UUID = userId!!
    fun validBookId(): UUID = bookId!!
    fun validBookIsbn(): String = bookIsbn!!
    fun validBookTitle(): String = bookTitle!!
    fun validBookAuthor(): String = bookAuthor!!
    fun validBookPublisher(): String = bookPublisher!!
    fun validBookCoverImageUrl(): String = bookCoverImageUrl!!
    fun validStatus(): BookStatus = status!!

    companion object {
        fun of(
            userId: UUID,
            bookCreateResponse: BookCreateResponse,
            status: BookStatus,
        ): UpsertUserBookRequest {
            return UpsertUserBookRequest(
                userId = userId,
                bookId = bookCreateResponse.bookId,
                bookIsbn = bookCreateResponse.isbn,
                bookTitle = bookCreateResponse.title,
                bookAuthor = bookCreateResponse.author,
                bookPublisher = bookCreateResponse.publisher,
                bookCoverImageUrl = bookCreateResponse.coverImageUrl,
                status = status
            )
        }
    }
}
