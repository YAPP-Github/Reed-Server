package org.yapp.apis.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import org.yapp.apis.auth.dto.response.UserIdResponse
import java.util.*

@Schema(
    name = "FindUserIdentityRequest",
    description = "Request DTO to retrieve user identity information using userId"
)
data class FindUserIdentityRequest private constructor(
    @Schema(
        description = "User ID (UUID format)",
        example = "a1b2c3d4-e5f6-7890-1234-56789abcdef0"
    )
    @field:NotNull(message = "userId must not be null")
    val userId: UUID? = null
) {
    fun validUserId(): UUID = userId!!

    companion object {
        fun from(userIdResponse: UserIdResponse): FindUserIdentityRequest {
            return FindUserIdentityRequest(userIdResponse.userId)
        }
    }
}
