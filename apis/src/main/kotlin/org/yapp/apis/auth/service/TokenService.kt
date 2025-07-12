package org.yapp.apis.auth.service

import org.springframework.stereotype.Service
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.domain.token.TokenDomainRedisService
import org.yapp.domain.token.RefreshToken
import java.util.*

@Service
class TokenService(
    private val tokenDomainRedisService: TokenDomainRedisService,
) {

    fun deleteByToken(token: String) {
        tokenDomainRedisService.deleteRefreshTokenByToken(token)
    }

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
