package org.yapp.apis.auth.service

import jakarta.validation.Valid
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import org.yapp.apis.auth.dto.request.TokenGenerateRequest
import org.yapp.apis.auth.dto.request.TokenRefreshRequest
import org.yapp.apis.auth.dto.response.RefreshTokenResponse
import org.yapp.apis.auth.dto.response.UserIdResponse
import org.yapp.domain.token.RefreshTokenDomainService
import java.util.*

@Service
@Validated
class RefreshTokenService(
    private val refreshTokenDomainService: RefreshTokenDomainService,
) {
    fun deleteRefreshTokenByToken(token: String) {
        refreshTokenDomainService.deleteRefreshTokenByToken(token)
    }

    fun saveRefreshToken(@Valid tokenGenerateRequest: TokenGenerateRequest): RefreshTokenResponse {
        val token = refreshTokenDomainService.saveRefreshToken(
            tokenGenerateRequest.validUserId(),
            tokenGenerateRequest.validRefreshToken(),
            tokenGenerateRequest.validExpiration()
        )
        return RefreshTokenResponse.from(token)
    }

    fun getRefreshTokenByUserId(userId: UUID): RefreshTokenResponse {
        val token = refreshTokenDomainService.getRefreshTokenByUserId(userId)
        return RefreshTokenResponse.from(token)
    }

    fun validateRefreshToken(refreshToken: String) {
        refreshTokenDomainService.validateRefreshTokenByToken(refreshToken)
    }

    fun getUserIdByToken(@Valid tokenRefreshRequest: TokenRefreshRequest): UserIdResponse {
        val userId = refreshTokenDomainService.getUserIdByToken(tokenRefreshRequest.validRefreshToken())
        return UserIdResponse.from(userId)
    }
}
