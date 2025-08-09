package org.yapp.apis.book.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import org.yapp.domain.userbook.BookStatus
import org.yapp.globalutils.util.RegexUtils

@Schema(
    name = "UserBookRegisterRequest",
    description = "사용자의 서재에 도서를 등록하거나 상태를 변경하는 요청"
)
data class UserBookRegisterRequest private constructor(
    @field:NotBlank(message = "ISBN13은 비어 있을 수 없습니다.")
    @field:Pattern(
        regexp = RegexUtils.ISBN13_PATTERN,
        message = "유효한 13자리 ISBN13 형식이 아닙니다."
    )
    @field:Schema(
        description = "등록할 책의 13자리 ISBN13 코드",
        example = "9788932473901",
        required = true,
        minLength = 13,
        maxLength = 13
    )
    val isbn13: String? = null,

    @field:NotNull(message = "도서 상태는 필수입니다.")
    @field:Schema(
        description = "사용자의 도서 읽기 상태",
        example = "READING",
        required = true,
        allowableValues = ["BEFORE_REGISTRATION", "BEFORE_READING", "READING", "COMPLETED"],
        enumAsRef = true
    )
    val bookStatus: BookStatus? = null
) {
    fun validIsbn13(): String = isbn13!!
    fun validBookStatus(): BookStatus = bookStatus!!
}
