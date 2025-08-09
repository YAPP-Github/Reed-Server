package org.yapp.apis.book.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.yapp.domain.book.vo.BookInfoVO
import java.util.UUID

@Schema(
    name = "BookCreateResponse",
    description = "책 생성 요청 처리 후 반환되는 정보"
)
data class BookCreateResponse private constructor(

    @field:Schema(description = "등록된 책의 UUID", example = "123e4567-e89b-12d3-a456-426614174000")
    val bookId: UUID,

    @field:Schema(description = "ISBN-13 번호", example = "9791164053353")
    val isbn13: String,

    @field:Schema(description = "책 제목", example = "데미안")
    val title: String,

    @field:Schema(description = "저자", example = "헤르만 헤세")
    val author: String,

    @field:Schema(description = "출판사", example = "북하우스")
    val publisher: String,

    @field:Schema(description = "책 표지 이미지 URL", example = "https://image.aladin.co.kr/product/36801/75/coversum/k692030806_1.jpg")
    val coverImageUrl: String
) {
    companion object {
        fun from(bookVO: BookInfoVO): BookCreateResponse {
            return BookCreateResponse(
                bookId = bookVO.id.value,
                isbn13 = bookVO.isbn13.value,
                title = bookVO.title,
                author = bookVO.author,
                publisher = bookVO.publisher,
                coverImageUrl = bookVO.coverImageUrl,
            )
        }
    }
}
