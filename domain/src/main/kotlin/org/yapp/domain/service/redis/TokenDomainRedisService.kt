package org.yapp.domain.service.redis

import org.yapp.globalutils.DomainService
import org.yapp.domain.auth.TokenRepository
import java.util.*

@DomainService
class TokenDomainRedisService(
    private val tokenRepository: TokenRepository
) {

    fun saveRefreshToken(userId: UUID, refreshToken: String, expiration: Long) {
        tokenRepository.saveRefreshToken(userId, refreshToken, expiration)
    }

    fun deleteRefreshToken(userId: UUID) {
        tokenRepository.deleteRefreshToken(userId)
    }

    fun validateRefreshToken(userId: UUID, refreshToken: String): Boolean {
        return tokenRepository.existsRefreshToken(userId, refreshToken)
    }
}
