package org.yapp.apis.auth.dto

data class TokenPair(
    val accessToken: String,
    val refreshToken: String
)