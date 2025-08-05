package org.yapp.apis.auth.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.yapp.apis.user.dto.response.CreateUserResponse
import org.yapp.apis.auth.strategy.AppleAuthCredentials
import java.util.UUID

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
        description = "Authorization code from Apple OAuth process",
        example = "cdef1234-abcd-5678-efgh-9012ijklmnop"
    )
    @field:NotBlank(message = "authorizationCode must not be blank")
    val authorizationCode: String? = null,

    @Schema(
        description = "Apple refresh token, nullable if not issued yet",
        example = "apple-refresh-token-example"
    )
    val appleRefreshToken: String? = null
) {
    fun validUserId(): UUID = userId!!
    fun validAuthorizationCode(): String = authorizationCode!!

    companion object {
        fun of(
            userResponse: CreateUserResponse,
            credentials: AppleAuthCredentials
        ): SaveAppleRefreshTokenRequest {
            return SaveAppleRefreshTokenRequest(
                userId = userResponse.id,
                authorizationCode = credentials.authorizationCode,
                appleRefreshToken = userResponse.appleRefreshToken
            )
        }
    }
}
