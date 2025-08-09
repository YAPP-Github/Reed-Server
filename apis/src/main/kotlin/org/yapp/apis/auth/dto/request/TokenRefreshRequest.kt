package org.yapp.apis.auth.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(
    name = "TokenRefreshRequest",
    description = "DTO for requesting an access token using a refresh token"
)
data class TokenRefreshRequest private constructor(
    @field:Schema(
        description = "Valid refresh token issued during previous authentication",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        required = true
    )
    @field:NotBlank(message = "Refresh token is required")
    val refreshToken: String? = null
) {
    fun validRefreshToken(): String = refreshToken!!
}
