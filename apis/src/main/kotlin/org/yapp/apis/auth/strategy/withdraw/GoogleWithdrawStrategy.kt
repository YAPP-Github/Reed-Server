package org.yapp.apis.auth.strategy.withdraw

import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.yapp.apis.auth.dto.request.WithdrawStrategyRequest
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.auth.manager.GoogleApiManager
import org.yapp.domain.user.ProviderType

@Component
class GoogleWithdrawStrategy(
    private val googleApiManager: GoogleApiManager
) : WithdrawStrategy {

    private val log = KotlinLogging.logger {}

    override fun getProviderType(): ProviderType = ProviderType.GOOGLE

    override fun withdraw(request: WithdrawStrategyRequest) {
        val googleRefreshToken = request.googleRefreshToken
            ?: throw AuthException(AuthErrorCode.GOOGLE_REFRESH_TOKEN_NOT_FOUND, "Google Refresh Token이 존재하지 않습니다.")

        try {
            googleApiManager.revokeToken(googleRefreshToken as String)
            log.info { "Google refresh token revoked successfully for user ${request.userId}" }
        } catch (e: Exception) {
            log.error("Failed to revoke Google token for user ${request.userId}", e)
            throw AuthException(AuthErrorCode.FAILED_TO_REVOKE_TOKEN, e.message)
        }
    }
}
