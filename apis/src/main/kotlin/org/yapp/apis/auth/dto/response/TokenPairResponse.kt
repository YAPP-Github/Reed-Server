package org.yapp.apis.auth.dto.response

data class TokenPairResponse(
    val accessToken: String,
    val refreshToken: String
)