package org.yapp.apis.book.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.yapp.globalutils.util.RegexUtils


data class BookDetailRequest private constructor(
    @field:NotBlank(message = "ISBN은 비어 있을 수 없습니다.")
    @field:Pattern(
        regexp = RegexUtils.ISBN13_PATTERN,
        message = "유효한 13자리 ISBN 형식이 아닙니다."
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
