package org.yapp.apis.auth.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import org.yapp.apis.auth.dto.response.CreateUserResponse
import org.yapp.apis.auth.dto.response.UserAuthInfoResponse
import org.yapp.globalutils.auth.Role
import java.util.*

@Schema(
    name = "GenerateTokenPairRequest",
    description = "Request DTO to generate a new pair of access and refresh tokens"
)
data class GenerateTokenPairRequest private constructor(
    @field:NotBlank(message = "userId must not be null")
    @Schema(
        description = "User ID",
        example = "a1b2c3d4-e5f6-7890-1234-56789abcdef0"
    )
    val userId: UUID? = null,

    @field:NotBlank(message = "role must not be null")
    @Schema(
        description = "User role",
        example = "USER"
    )
    val role: Role? = null
) {

    fun validUserId(): UUID = userId!!
    fun validRole(): Role = role!!

    companion object {
        fun from(response: CreateUserResponse): GenerateTokenPairRequest {
            return GenerateTokenPairRequest(
                userId = response.id,
                role = response.role
            )
        }

        fun from(response: UserAuthInfoResponse): GenerateTokenPairRequest {
            return GenerateTokenPairRequest(
                userId = response.id,
                role = response.role
            )
        }
    }
}
