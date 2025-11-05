package org.yapp.apis.auth.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.yapp.domain.user.User
import java.util.*

@Schema(
    name = "UserIdResponse",
    description = "Response DTO that contains the user ID extracted from a refresh token"
)
data class UserIdResponse(
    @field:Schema(description = "User ID", example = "a1b2c3d4-e5f6-7890-1234-56789abcdef0")
    val userId: UUID
) {
    companion object {
        fun from(userId: User.Id): UserIdResponse {
            return UserIdResponse(userId.value)
        }
    }
}
