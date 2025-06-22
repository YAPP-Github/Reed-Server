package org.yapp.apis.token.service

import org.springframework.stereotype.Service
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.domain.redisservice.TokenDomainRedisService
import java.util.*

@Service
class TokenService(
    private val tokenDomainserviceRedis: TokenDomainRedisService,
) {

    fun delete(userId: UUID) {
        tokenDomainserviceRedis.deleteRefreshToken(userId)
    }

    fun save(userId: UUID, refreshToken: String, expiration: Long) {
        tokenDomainserviceRedis.saveRefreshToken(userId, refreshToken, expiration)
    }

    fun validateRefreshTokenOrThrow(userId: UUID, refreshToken: String) {
        val exists = tokenDomainserviceRedis.validateRefreshToken(userId, refreshToken)
        if (!exists) {
            throw AuthException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND)
        }
    }
}
