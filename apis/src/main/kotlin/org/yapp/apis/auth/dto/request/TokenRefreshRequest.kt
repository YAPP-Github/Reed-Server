package org.yapp.apis.auth.dto.request

data class TokenRefreshRequest(
    val refreshToken: String
)