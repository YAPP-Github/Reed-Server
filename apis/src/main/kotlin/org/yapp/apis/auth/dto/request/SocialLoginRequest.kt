package org.yapp.apis.auth.dto.request

import jakarta.validation.constraints.NotBlank
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.auth.service.AppleAuthCredentials
import org.yapp.apis.auth.service.AuthCredentials
import org.yapp.apis.auth.service.KakaoAuthCredentials
import org.yapp.domain.auth.ProviderType

data class SocialLoginRequest(
    @field:NotBlank(message = "Provider type is required")
    val providerType: ProviderType,

    @field:NotBlank(message = "OAuth token is required")
    val oauthToken: String
) {
    companion object {
        fun toCredentials(request: SocialLoginRequest): AuthCredentials {
            return when (request.providerType) {
                ProviderType.KAKAO -> KakaoAuthCredentials(request.oauthToken)
                ProviderType.APPLE -> AppleAuthCredentials(request.oauthToken)
                else -> throw AuthException(
                    AuthErrorCode.UNSUPPORTED_PROVIDER_TYPE,
                    "Unsupported provider type: ${request.providerType}"
                )
            }
        }
    }
}
