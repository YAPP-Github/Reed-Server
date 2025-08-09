package org.yapp.apis.auth.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "TokenPairResponse",
    description = "Token pair consisting of access and refresh tokens"
)
data class TokenPairResponse private constructor(

    @field:Schema(
        description = "Access token for user authorization",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    val accessToken: String,

    @field:Schema(
        description = "Refresh token to get new access token",
        example = "dGhpc2lzYXJlZnJlc2h0b2tlbg=="
    )
    val refreshToken: String
) {
    companion object {
        fun of(
            accessToken: String,
            refreshToken: String
        ): TokenPairResponse {
            return TokenPairResponse(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        }
    }
}
