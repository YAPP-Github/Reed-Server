package org.yapp.domain.token

import org.yapp.globalutils.annotation.DomainService
import java.time.LocalDateTime
import java.util.UUID

@DomainService
class TokenDomainRedisService(
    private val refreshTokenRepository: RefreshTokenRepository
) {
    fun saveRefreshToken(userId: UUID, refreshToken: String, expiration: Long) {
        val expiresAt = LocalDateTime.now().plusSeconds(expiration)
        val token = RefreshToken.create(
            token = refreshToken,
            userId = userId,
            expiresAt = expiresAt,
            createdAt = LocalDateTime.now()
        )
        refreshTokenRepository.save(token)
    }

    fun deleteRefreshTokenByToken(token: String) {
        refreshTokenRepository.deleteByToken(token)
    }

    fun validateRefreshTokenByToken(refreshToken: String): Boolean {
        val storedToken = refreshTokenRepository.findByToken(refreshToken)
        return storedToken != null && storedToken.expiresAt.isAfter(LocalDateTime.now())
    }

    fun getUserIdFromToken(refreshToken: String): UUID? {
        val storedToken = refreshTokenRepository.findByToken(refreshToken)
        return storedToken?.userId
    }

    fun getRefreshTokenByUserId(userId: UUID): RefreshToken? {
        return refreshTokenRepository.findByUserId(userId)
    }
}
