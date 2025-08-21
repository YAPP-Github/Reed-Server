package org.yapp.infra.external.oauth.apple.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AppleTokenResponse(
    val accessToken: String,
    val expiresIn: Int,
    val idToken: String,
    val refreshToken: String?,
    val tokenType: String
)
