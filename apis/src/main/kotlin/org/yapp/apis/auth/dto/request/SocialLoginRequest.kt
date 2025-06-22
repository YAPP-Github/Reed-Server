package org.yapp.apis.auth.dto.request

import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.auth.service.AppleAuthCredentials
import org.yapp.apis.auth.service.AuthCredentials
import org.yapp.apis.auth.service.KakaoAuthCredentials
import org.yapp.domain.auth.ProviderType

data class SocialLoginRequest(
    val providerType: String,
    val oauthToken: String
) {

    init {
        require(providerType.isNotBlank()) {
            "Provider type must not be blank"
        }
        require(oauthToken.isNotBlank()) {
            "Access token must not be blank"
        }
    }

    fun toCredentials(): AuthCredentials {
        val provider = runCatching {
            ProviderType.valueOf(providerType.uppercase())
        }.getOrElse {
            throw AuthException(
                AuthErrorCode.UNSUPPORTED_PROVIDER_TYPE,
                "Invalid provider type: $providerType"
            )
        }

        return when (provider) {
            ProviderType.KAKAO -> KakaoAuthCredentials(oauthToken)
            ProviderType.APPLE -> AppleAuthCredentials(oauthToken)
        }
    }


}
