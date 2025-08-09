package org.yapp.apis.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.yapp.apis.user.dto.response.CreateUserResponse
import java.util.*

@Schema(
    name = "SaveAppleRefreshTokenRequest",
    description = "Request DTO for saving Apple refresh token with user ID and authorization code"
)
data class SaveAppleRefreshTokenRequest private constructor(
    @Schema(
        description = "Unique identifier of the user",
        example = "a1b2c3d4-e5f6-7890-1234-56789abcdef0"
    )
    @field:NotNull(message = "userId must not be null")
    val userId: UUID? = null,

    @Schema(
        description = "Apple refresh token, nullable if not issued yet",
        example = "apple-refresh-token-example"
    )
    @field:NotBlank(message = "appleRefreshToken must not be blank")
    val appleRefreshToken: String? = null
) {
    fun validUserId(): UUID = userId!!
    fun validAppleRefreshToken(): String = appleRefreshToken!!

    companion object {
        fun of(
            userResponse: CreateUserResponse,
            appleRefreshToken: String
        ): SaveAppleRefreshTokenRequest {
            return SaveAppleRefreshTokenRequest(
                userId = userResponse.id,
                appleRefreshToken = appleRefreshToken
            )
        }
    }
}
