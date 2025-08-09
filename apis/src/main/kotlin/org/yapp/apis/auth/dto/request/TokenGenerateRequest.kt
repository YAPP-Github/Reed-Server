package org.yapp.apis.auth.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.*

@Schema(
    name = "TokenGenerateRequest",
    description = "DTO containing information required to save the generated refresh token"
)
data class TokenGenerateRequest private constructor(
    @field:NotNull(message = "userId must not be null")
    @field:Schema(description = "User ID", example = "f6b7d490-1b1a-4b9f-8e8e-27f8e3a5dafa")
    val userId: UUID? = null,

    @field:NotBlank(message = "refreshToken must not be blank")
    @field:Schema(description = "Generated refresh token", example = "eyJhbGciOiJIUzI1NiIsInR...")
    val refreshToken: String? = null,

    @field:NotNull(message = "expiration must not be null")
    @field:Schema(description = "Refresh token expiration time (in seconds)", example = "2592000")
    val expiration: Long? = null
) {
    fun validUserId() = userId!!
    fun validRefreshToken() = refreshToken!!
    fun validExpiration() = expiration!!

    companion object {
        fun of(userId: UUID, refreshToken: String, expiration: Long): TokenGenerateRequest {
            require(refreshToken.isNotBlank()) { "Refresh token must not be blank." }
            require(expiration > 0) { "Expiration must be greater than 0." }

            return TokenGenerateRequest(
                userId = userId,
                refreshToken = refreshToken,
                expiration = expiration
            )
        }
    }
}
