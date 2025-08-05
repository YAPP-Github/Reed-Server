package org.yapp.apis.auth.service

import jakarta.validation.Valid
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import org.yapp.apis.auth.dto.request.DeleteTokenRequest
import org.yapp.apis.auth.dto.request.GenerateTokenPairRequest
import org.yapp.apis.auth.dto.request.TokenGenerateRequest
import org.yapp.apis.auth.dto.request.TokenRefreshRequest
import org.yapp.apis.auth.dto.response.TokenPairResponse
import org.yapp.apis.auth.dto.response.UserIdResponse
import org.yapp.gateway.jwt.JwtTokenService

@Service
@Validated
class AuthTokenService(
    private val refreshTokenService: RefreshTokenService,
    private val jwtTokenService: JwtTokenService
) {
    fun generateTokenPair(@Valid generateTokenPairRequest: GenerateTokenPairRequest): TokenPairResponse {
        val userId = generateTokenPairRequest.validUserId()
        val role = generateTokenPairRequest.validRole()

        val accessToken = jwtTokenService.generateAccessToken(userId, role)
        val refreshToken = jwtTokenService.generateRefreshToken(userId)
        val expiration = jwtTokenService.getRefreshTokenExpiration()

        val refreshTokenResponse = refreshTokenService.saveRefreshToken(
            TokenGenerateRequest.of(userId, refreshToken, expiration)
        )

        return TokenPairResponse.of(accessToken, refreshTokenResponse.refreshToken)
    }

    fun validateAndGetUserIdFromRefreshToken(@Valid tokenRefreshRequest: TokenRefreshRequest): UserIdResponse {
        refreshTokenService.validateRefreshToken(tokenRefreshRequest.validRefreshToken())
        return refreshTokenService.getUserIdByToken(tokenRefreshRequest)
    }

    fun deleteTokenForReissue(@Valid tokenRefreshRequest: TokenRefreshRequest) {
        refreshTokenService.deleteRefreshTokenByToken(tokenRefreshRequest.validRefreshToken())
    }

    fun deleteTokenForSignOut(@Valid deleteTokenRequest: DeleteTokenRequest) {
        refreshTokenService.deleteRefreshTokenByToken(deleteTokenRequest.validRefreshToken())
    }
}
