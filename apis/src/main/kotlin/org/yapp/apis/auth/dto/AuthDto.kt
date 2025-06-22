package org.yapp.apis.auth.dto

import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.auth.service.AppleAuthCredentials
import org.yapp.apis.auth.service.AuthCredentials
import org.yapp.apis.auth.service.KakaoAuthCredentials
import org.yapp.domain.auth.ProviderType

/**
 * DTOs for authentication requests and responses.
 */
object AuthDto {
    /**
     * Request for social login.
     */
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

        /**
         * Convert to AuthCredentials.
         *
         * @return The AuthCredentials.
         */
        fun toCredentials(): AuthCredentials {
            return when (ProviderType.valueOf(providerType.uppercase())) {
                ProviderType.KAKAO -> KakaoAuthCredentials(oauthToken)
                ProviderType.APPLE -> AppleAuthCredentials(oauthToken)
                else -> throw AuthException(
                    AuthErrorCode.UNSUPPORTED_PROVIDER_TYPE,
                    "Unsupported provider type: $providerType"
                )
            }
        }
    }

    /**
     * Request for token refresh.
     */
    data class TokenRefreshRequest(
        val refreshToken: String
    )

    /**
     * Response for authentication.
     */
    data class AuthResponse(
        val accessToken: String,
        val refreshToken: String
    ) {
        companion object {
            /**
             * Create from TokenPair.
             *
             * @param tokenPair The TokenPair.
             * @return The AuthResponse.
             */
            fun fromTokenPair(tokenPair: TokenPair): AuthResponse {
                return AuthResponse(
                    accessToken = tokenPair.accessToken,
                    refreshToken = tokenPair.refreshToken
                )
            }
        }
    }
}
