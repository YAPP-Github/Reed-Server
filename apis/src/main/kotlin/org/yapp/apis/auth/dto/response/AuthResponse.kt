package org.yapp.apis.auth.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "AuthResponse",
    description = "Authentication response containing access and refresh tokens"
)
data class AuthResponse private constructor(

    @field:Schema(
        description = "Access token for authorization",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    val accessToken: String,

    @field:Schema(
        description = "Refresh token used to obtain a new access token",
        example = "dGhpc2lzYXJlZnJlc2h0b2tlbg=="
    )
    val refreshToken: String
) {
    companion object {
        fun fromTokenPair(tokenPairResponse: TokenPairResponse)
        : AuthResponse {
            return AuthResponse(
                accessToken = tokenPairResponse.accessToken,
                refreshToken = tokenPairResponse.refreshToken
            )
        }
    }
}
