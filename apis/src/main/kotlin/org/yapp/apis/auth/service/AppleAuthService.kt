package org.yapp.apis.auth.service

import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.auth.manager.AppleApiManager
import org.yapp.infra.external.oauth.apple.response.AppleTokenResponse

@Service
@Validated
class AppleAuthService(
    private val appleApiManager: AppleApiManager,
) {
    fun fetchAppleOauthTokens(authorizationCode: String): AppleTokenResponse {
        val tokenResponse = appleApiManager.fetchAppleOauthTokens(authorizationCode)

        tokenResponse.refreshToken
            ?: throw AuthException(AuthErrorCode.MISSING_APPLE_REFRESH_TOKEN)

        return tokenResponse
    }
}
