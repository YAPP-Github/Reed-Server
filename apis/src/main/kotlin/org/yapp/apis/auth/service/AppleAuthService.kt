package org.yapp.apis.auth.service

import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.auth.manager.AppleApiManager
import org.yapp.globalutils.annotation.ApplicationService
import org.yapp.infra.external.oauth.apple.response.AppleTokenResponse

@ApplicationService
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
