package org.yapp.domain.token

import org.yapp.domain.token.RefreshToken.Token
import org.yapp.domain.token.exception.TokenErrorCode
import org.yapp.domain.token.exception.TokenNotFoundException
import org.yapp.domain.user.User
import org.yapp.globalutils.annotation.DomainService
import java.time.LocalDateTime
import java.util.*

@DomainService
class RefreshTokenDomainService(
    private val refreshTokenRepository: RefreshTokenRepository
) {
    fun saveRefreshToken(userId: UUID, refreshToken: String, expiration: Long): Token {
        val now = LocalDateTime.now()
        val token = RefreshToken.create(
            token = refreshToken,
            userId = userId,
            expiresAt = now.plusSeconds(expiration),
            createdAt = now
        )
        val savedRefreshToken = refreshTokenRepository.save(token)
        return savedRefreshToken.token
    }

    fun deleteRefreshTokenByToken(token: String) {
        refreshTokenRepository.deleteByToken(token)
    }

    fun validateRefreshTokenByToken(refreshToken: String) {
        val storedToken = refreshTokenRepository.findByToken(refreshToken)
            ?: throw TokenNotFoundException(TokenErrorCode.TOKEN_NOT_FOUND)

        if (storedToken.isExpired()) {
            throw TokenNotFoundException(TokenErrorCode.EXPIRED_REFRESH_TOKEN)
        }
    }

    fun getUserIdByToken(refreshToken: String): User.Id {
        val storedToken = refreshTokenRepository.findByToken(refreshToken)
            ?: throw TokenNotFoundException(TokenErrorCode.TOKEN_NOT_FOUND)
        return storedToken.userId
    }

    fun getRefreshTokenByUserId(userId: UUID): Token {
        val refreshToken = refreshTokenRepository.findByUserId(userId)
            ?: throw TokenNotFoundException(TokenErrorCode.TOKEN_NOT_FOUND)

        return refreshToken.token
    }
}
