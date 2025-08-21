package org.yapp.apis.book.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.util.UUID

@Schema(
    name = "UserBooksByIsbn13sRequest",
    description = "Request DTO for finding user books by user ID and a list of ISBN13s"
)
data class UserBooksByIsbn13sRequest private constructor(
    @field:Schema(
        description = "사용자 ID",
        example = "1"
    )
    @field:NotNull(message = "userId는 필수입니다.")
    val userId: UUID? = null,

    @field:Schema(
        description = "도서 ISBN 목록",
        example = "[\"9788966262429\", \"9791190412351\"]"
    )
    @field:NotEmpty(message = "isbn13 리스트는 비어있을 수 없습니다.")
    val isbn13s: List<String>? = null

) {
    fun validUserId(): UUID = userId!!
    fun validIsbn13s(): List<String> = isbn13s!!

    companion object {
        fun of(userId: UUID, isbn13s: List<String>): UserBooksByIsbn13sRequest {
            return UserBooksByIsbn13sRequest(userId = userId, isbn13s = isbn13s)
        }
    }
}
