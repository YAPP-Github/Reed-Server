package org.yapp.apis.auth.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.user.service.UserAccountService
import java.util.*

@Service
class UserWithdrawalService(
    private val userAccountService: UserAccountService,
    private val refreshTokenService: RefreshTokenService
) {
    @Transactional
    fun processWithdrawal(userId: UUID) {
        val refreshTokenResponse = refreshTokenService.getRefreshTokenByUserId(userId)
        refreshTokenService.deleteRefreshTokenByToken(refreshTokenResponse.refreshToken)

        userAccountService.withdrawUser(userId)
    }
}
