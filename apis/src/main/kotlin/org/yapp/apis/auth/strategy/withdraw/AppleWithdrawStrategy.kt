package org.yapp.apis.auth.strategy.withdraw

import jakarta.validation.Valid
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import org.yapp.apis.auth.dto.request.WithdrawStrategyRequest
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.auth.manager.AppleApiManager
import org.yapp.domain.user.ProviderType

@Component
@Validated
class AppleWithdrawStrategy(
    private val appleApiManager: AppleApiManager
) : WithdrawStrategy {
    override fun getProviderType(): ProviderType = ProviderType.APPLE

    override fun withdraw(@Valid request: WithdrawStrategyRequest) {
        val appleRefreshToken = request.appleRefreshToken
            ?: throw AuthException(AuthErrorCode.APPLE_REFRESH_TOKEN_MISSING)

        appleApiManager.revokeToken(appleRefreshToken)
    }
}
