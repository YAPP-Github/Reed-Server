package org.yapp.apis.auth.dto

import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.auth.service.AppleAuthCredentials
import org.yapp.apis.auth.service.AuthCredentials
import org.yapp.apis.auth.service.KakaoAuthCredentials
import org.yapp.apis.auth.service.TokenPair

/**
 * DTOs for authentication requests and responses.
 */
object AuthDto {
    /**
     * Request for social login.
     */
    data class SocialLoginRequest(
        val providerType: String,
        val accessToken: String
    ) {
        /**
         * Convert to AuthCredentials.
         *
         * @return The AuthCredentials.
         */
        fun toCredentials(): AuthCredentials {
            return when (providerType.uppercase()) {
                "KAKAO" -> KakaoAuthCredentials(accessToken)
                "APPLE" -> AppleAuthCredentials(accessToken)
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
