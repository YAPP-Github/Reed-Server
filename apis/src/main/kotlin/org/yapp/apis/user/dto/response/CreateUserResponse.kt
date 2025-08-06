package org.yapp.apis.user.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.yapp.domain.user.vo.UserAuthVO
import org.yapp.globalutils.auth.Role
import java.util.*

@Schema(
    name = "CreateUserResponse",
    description = "Response DTO returned after successful user registration"
)
data class CreateUserResponse private constructor(
    @Schema(
        description = "사용자 ID",
        example = "a1b2c3d4-e5f6-7890-1234-56789abcdef0"
    )
    val id: UUID,

    @Schema(
        description = "사용자 역할",
        example = "USER"
    )
    val role: Role,

    @Schema(
        description = "Apple Refresh Token (Apple 유저인 경우에만 존재)",
        nullable = true
    )
    val appleRefreshToken: String?
) {
    companion object {
        fun from(auth: UserAuthVO): CreateUserResponse {
            return CreateUserResponse(
                id = auth.id.value,
                role = auth.role,
                appleRefreshToken = auth.appleRefreshToken
            )
        }
    }
}
