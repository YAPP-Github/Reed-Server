package org.yapp.apis.auth.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.yapp.domain.user.vo.UserIdentity
import org.yapp.globalutils.auth.Role
import java.util.*

@Schema(
    name = "CreateUserResponse",
    description = "Response DTO returned after successful user registration"
)
data class CreateUserResponse private constructor(
    @Schema(description = "사용자 ID", example = "a1b2c3d4-e5f6-7890-1234-56789abcdef0")
    val id: UUID,

    @Schema(description = "사용자 역할", example = "USER")
    val role: Role
) {
    companion object {
        fun from(identity: UserIdentity): CreateUserResponse {
            return CreateUserResponse(
                id = identity.id,
                role = identity.role
            )
        }
    }
}
