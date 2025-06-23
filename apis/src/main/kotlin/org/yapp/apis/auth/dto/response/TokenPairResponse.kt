package org.yapp.apis.auth.dto.response

data class TokenPairResponse(
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        fun from(accessToken: String, refreshToken: String): TokenPairResponse {
            return TokenPairResponse(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        }
    }
}
