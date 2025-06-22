package org.yapp.apis.auth.dto.request

import jakarta.validation.constraints.NotBlank

data class TokenRefreshRequest(
    @field:NotBlank(message = "Refresh token is required")
    val refreshToken: String
)
