package org.yapp.apis.auth.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.user.service.UserAccountService
import java.util.*

@Service
class UserWithdrawalService(
    private val userAccountService: UserAccountService,
    private val refreshTokenService: RefreshTokenService
) {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun processWithdrawal(userId: UUID) {
        userAccountService.withdrawUser(userId)

        val refreshTokenResponse = refreshTokenService.getRefreshTokenByUserId(userId)
        refreshTokenService.deleteRefreshTokenByToken(refreshTokenResponse.refreshToken)
    }
}
