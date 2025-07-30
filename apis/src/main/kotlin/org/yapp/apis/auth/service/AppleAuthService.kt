package org.yapp.apis.auth.service

import org.springframework.stereotype.Service
import org.yapp.apis.auth.dto.request.SaveAppleRefreshTokenRequest
import org.yapp.apis.auth.manager.AppleApiManager
import org.yapp.domain.user.UserDomainService

@Service
class AppleAuthService(
    private val appleApiManager: AppleApiManager,
    private val userDomainService: UserDomainService
) {
    fun saveAppleRefreshTokenIfMissing(request: SaveAppleRefreshTokenRequest) {
        if (request.appleRefreshToken == null) {
            val tokenResponse = appleApiManager.fetchAppleOauthTokens(request.validAuthorizationCode())
            userDomainService.updateAppleRefreshToken(request.validUserId(), tokenResponse.refreshToken)
        }
    }
}
