package org.yapp.apis.auth.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.yapp.domain.user.vo.UserIdentity
import org.yapp.globalutils.auth.Role
import java.util.*

@Schema(
    name = "UserAuthInfoResponse",
    description = "Response DTO containing minimal authentication information (ID and role)"
)
data class UserAuthInfoResponse private constructor(
    @Schema(
        description = "Unique identifier of the user",
        example = "a1b2c3d4-e5f6-7890-1234-56789abcdef0"
    )
    val id: UUID,

    @Schema(
        description = "Role assigned to the user",
        example = "USER"
    )
    val role: Role
) {
    companion object {
        fun from(identity: UserIdentity): UserAuthInfoResponse {
            return UserAuthInfoResponse(
                id = identity.id,
                role = identity.role
            )
        }
    }
}
