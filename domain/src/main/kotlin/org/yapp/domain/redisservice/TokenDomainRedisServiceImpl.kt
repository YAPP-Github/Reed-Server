package org.yapp.domain.redisservice


import org.yapp.annotation.DomainService
import org.yapp.domain.auth.TokenRepository
import java.util.*

@DomainService
class TokenDomainRedisServiceImpl(
    private val tokenRepository: TokenRepository
) : TokenDomainRedisService {

    override fun saveRefreshToken(userId: UUID, refreshToken: String, expiration: Long) {
        tokenRepository.saveRefreshToken(userId, refreshToken, expiration)
    }

    override fun deleteRefreshToken(userId: UUID) {
        tokenRepository.deleteRefreshToken(userId)
    }

    override fun validateRefreshToken(userId: UUID, refreshToken: String): Boolean {
        return tokenRepository.existsRefreshToken(userId, refreshToken)
    }
}
