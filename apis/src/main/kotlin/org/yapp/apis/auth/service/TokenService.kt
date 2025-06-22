package org.yapp.apis.token.service

import org.springframework.stereotype.Service
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.domain.auth.TokenRepository

@Service
class TokenService(
    private val tokenRepository: TokenRepository,
) {

    fun delete(userId: Long) {
        tokenRepository.deleteRefreshToken(userId)
    }

    fun save(userId: Long, refreshToken: String, expiration: Long) {
        tokenRepository.saveRefreshToken(userId, refreshToken, expiration)
    }

    fun validateRefreshTokenOrThrow(userId: Long, refreshToken: String) {
        val exists = tokenRepository.existsRefreshToken(userId, refreshToken)
        if (!exists) {
            throw AuthException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND)
        }
    }
}
