package org.yapp.apis.book.dto.request

import jakarta.validation.constraints.NotBlank
import org.yapp.domain.userbook.BookStatus


data class UserBookRegisterRequest private constructor(
    @field:NotBlank(message = "ISBN은 필수입니다.")
    val bookIsbn: String? = null,

    val bookStatus: BookStatus
) {
    fun validBookIsbn(): String = bookIsbn!!

    companion object {
        fun create(bookIsbn: String, bookStatus: BookStatus): UserBookRegisterRequest {
            return UserBookRegisterRequest(bookIsbn, bookStatus)
        }
    }
}
