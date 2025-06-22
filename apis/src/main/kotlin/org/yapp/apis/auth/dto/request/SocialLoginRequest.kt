package org.yapp.apis.auth.dto.request

import jakarta.validation.constraints.NotBlank

data class SocialLoginRequest(
    @field:NotBlank(message = "Provider type is required")
    val providerType: String,

    @field:NotBlank(message = "OAuth token is required")
    val oauthToken: String
)
