package org.yapp.apis.auth.dto.request

import jakarta.validation.constraints.NotBlank
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.auth.service.AppleAuthCredentials
import org.yapp.apis.auth.service.AuthCredentials
import org.yapp.apis.auth.service.KakaoAuthCredentials

data class SocialLoginRequest(
    @field:NotBlank(message = "Provider type is required")
    val providerType: String,

    @field:NotBlank(message = "OAuth token is required")
    val oauthToken: String
) {
    fun toCredentials(): AuthCredentials {
        return when (providerType.uppercase()) {
            "KAKAO" -> KakaoAuthCredentials(oauthToken)
            "APPLE" -> AppleAuthCredentials(oauthToken)
            else -> throw AuthException(
                AuthErrorCode.UNSUPPORTED_PROVIDER_TYPE,
                "Unsupported provider type: $providerType"
            )
        }
    }
}
