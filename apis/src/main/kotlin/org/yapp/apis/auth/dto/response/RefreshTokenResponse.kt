package org.yapp.apis.auth.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.yapp.domain.token.RefreshToken.Token

@Schema(
    name = "RefreshTokenResponse",
    description = "Response DTO containing the issued refresh token"
)
data class RefreshTokenResponse(
    @field:Schema(description = "The refresh token string", example = "eyJhbGciOiJIUz...")
    val refreshToken: String
) {
    companion object {
        fun from(token: Token): RefreshTokenResponse {
            return RefreshTokenResponse(refreshToken = token.value)
        }
    }
}
