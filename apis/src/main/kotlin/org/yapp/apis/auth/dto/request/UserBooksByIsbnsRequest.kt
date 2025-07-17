package org.yapp.apis.auth.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import java.util.UUID

@Schema(
    name = "UserBooksByIsbnsRequest",
    description = "Request DTO for finding user books by user ID and a list of ISBNs"
)
data class UserBooksByIsbnsRequest(
    @Schema(
        description = "사용자 ID",
        example = "1"
    )
    @field:NotBlank(message = "userId는 필수입니다.")
    val userId: UUID? = null,

    @Schema(
        description = "도서 ISBN 목록",
        example = "[\"9788966262429\", \"9791190412351\"]"
    )
    @field:NotEmpty(message = "isbns는 비어있을 수 없습니다.")
    val isbns: List<String>? = null

) {


    fun validUserId(): UUID = userId!!
    fun validIsbns(): List<String> = isbns!!

    companion object {
        fun of(userId: UUID, isbns: List<String>): UserBooksByIsbnsRequest {
            return UserBooksByIsbnsRequest(userId = userId, isbns = isbns)
        }
    }
}
