package org.yapp.apis.auth.dto.response

/**
 * Response for authentication.
 */
data class AuthResponse private constructor(
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        fun fromTokenPair(tokenPairResponse: TokenPairResponse): AuthResponse {
            return AuthResponse(
                accessToken = tokenPairResponse.accessToken,
                refreshToken = tokenPairResponse.refreshToken
            )
        }
    }
}
