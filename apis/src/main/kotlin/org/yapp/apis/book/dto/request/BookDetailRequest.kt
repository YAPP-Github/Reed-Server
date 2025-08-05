package org.yapp.apis.book.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.yapp.globalutils.util.RegexUtils

@Schema(
    title = "책 상세 정보 요청",
    description = "특정 ISBN을 통한 책 상세 정보 조회 요청"
)
data class BookDetailRequest private constructor(
    @field:NotBlank(message = "ISBN은 비어 있을 수 없습니다.")
    @field:Pattern(
        regexp = RegexUtils.ISBN13_PATTERN,
        message = "유효한 13자리 ISBN 형식이 아닙니다."
    )
    @Schema(
        description = "조회할 책의 13자리 ISBN 코드",
        example = "9788932473901",
        required = true,
        pattern = "\\d{13}",
        minLength = 13,
        maxLength = 13
    )
    val isbn: String? = null,
) {
    fun validIsbn(): String = isbn!!

    companion object {
        fun from(
            isbn: String?,
        ): BookDetailRequest {
            return BookDetailRequest(
                isbn = isbn,
            )
        }
    }
}
