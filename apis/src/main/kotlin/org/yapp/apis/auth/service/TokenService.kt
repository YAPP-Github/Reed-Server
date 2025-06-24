package org.yapp.apis.auth.service

import org.springframework.stereotype.Service
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.domain.redisservice.TokenDomainRedisService
import java.util.*

@Service
class TokenService(
    private val tokenDomainRedisService: TokenDomainRedisService,
) {

    fun delete(userId: UUID) {
        tokenDomainRedisService.deleteRefreshToken(userId)
    }

    fun save(userId: UUID, refreshToken: String, expiration: Long) {
        tokenDomainRedisService.saveRefreshToken(userId, refreshToken, expiration)
    }

    fun validateRefreshTokenOrThrow(userId: UUID, refreshToken: String) {
        val exists = tokenDomainRedisService.validateRefreshToken(userId, refreshToken)
        if (!exists) {
            throw AuthException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND)
        }
    }
}
