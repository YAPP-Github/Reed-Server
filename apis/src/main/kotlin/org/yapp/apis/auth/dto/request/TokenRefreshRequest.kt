package org.yapp.apis.auth.dto.request

data class TokenRefreshRequest(
    val refreshToken: String
) {
    init {
        require(refreshToken.isNotBlank()) {
            "Refresh token must not be blank"
        }
    }
}
