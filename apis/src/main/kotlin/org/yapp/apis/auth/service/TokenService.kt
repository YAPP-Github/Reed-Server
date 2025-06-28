package org.yapp.apis.auth.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.domain.service.redis.TokenDomainRedisService
import org.yapp.domain.token.RefreshToken
import java.util.*

@Service
@Transactional(readOnly = true)
class TokenService(
    private val tokenDomainRedisService: TokenDomainRedisService,
) {

    @Transactional
    fun deleteByToken(token: String) {
        tokenDomainRedisService.deleteRefreshTokenByToken(token)
    }

    @Transactional
    fun save(userId: UUID, refreshToken: String, expiration: Long) {
        tokenDomainRedisService.saveRefreshToken(userId, refreshToken, expiration)
    }

    fun getRefreshTokenByUserId(userId: UUID): RefreshToken {
        return tokenDomainRedisService.getRefreshTokenByUserId(userId)
            ?: throw AuthException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND)
    }

    fun validateRefreshTokenByTokenOrThrow(refreshToken: String) {
        val exists = tokenDomainRedisService.validateRefreshTokenByToken(refreshToken)
        if (!exists) {
            throw AuthException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND)
        }
    }

    fun getUserIdFromToken(refreshToken: String): UUID {
        return tokenDomainRedisService.getUserIdFromToken(refreshToken)
            ?: throw AuthException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND)
    }
}
