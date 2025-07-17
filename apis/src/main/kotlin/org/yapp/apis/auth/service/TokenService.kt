package org.yapp.apis.auth.service

import org.springframework.stereotype.Service
import org.yapp.apis.auth.dto.request.TokenGenerateRequest
import org.yapp.apis.auth.dto.request.TokenRefreshRequest
import org.yapp.apis.auth.dto.response.RefreshTokenResponse
import org.yapp.apis.auth.dto.response.UserIdResponse
import org.yapp.domain.token.TokenDomainRedisService
import java.util.*

@Service
class TokenService(
    private val tokenDomainRedisService: TokenDomainRedisService,
) {
    fun deleteRefreshTokenByToken(token: String) {
        tokenDomainRedisService.deleteRefreshTokenByToken(token)
    }

    fun saveRefreshToken(tokenGenerateRequest: TokenGenerateRequest): RefreshTokenResponse {
        val token = tokenDomainRedisService.saveRefreshToken(
            tokenGenerateRequest.validUserId(),
            tokenGenerateRequest.validRefreshToken(),
            tokenGenerateRequest.validExpiration()
        )
        return RefreshTokenResponse.from(token)
    }

    fun getRefreshTokenByUserId(userId: UUID): RefreshTokenResponse {
        val token = tokenDomainRedisService.getRefreshTokenByUserId(userId)
        return RefreshTokenResponse.from(token)
    }

    fun validateRefreshToken(refreshToken: String) {
        tokenDomainRedisService.validateRefreshTokenByToken(refreshToken)
    }

    fun getUserIdByToken(tokenRefreshRequest: TokenRefreshRequest): UserIdResponse {
        val userId = tokenDomainRedisService.getUserIdByToken(tokenRefreshRequest.validRefreshToken())
        return UserIdResponse.from(userId)
    }
}
