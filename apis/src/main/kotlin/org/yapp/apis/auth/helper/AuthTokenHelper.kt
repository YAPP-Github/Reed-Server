package org.yapp.apis.auth.helper

import org.springframework.stereotype.Component
import org.yapp.apis.auth.dto.response.TokenPairResponse
import org.yapp.apis.auth.service.TokenService
import org.yapp.gateway.jwt.JwtTokenService
import java.util.*

@Component
class AuthTokenHelper(
    private val tokenService: TokenService,
    private val jwtTokenService: JwtTokenService
) {

    fun generateTokenPair(userId: UUID): TokenPairResponse {
        val accessToken = jwtTokenService.generateAccessToken(userId)
        val refreshToken = jwtTokenService.generateRefreshToken(userId)
        val expiration = jwtTokenService.getRefreshTokenExpiration()

        tokenService.save(userId, refreshToken, expiration)
        return TokenPairResponse.of(accessToken, refreshToken)
    }

    fun validateAndGetUserIdFromRefreshToken(refreshToken: String): UUID {
        val userId = jwtTokenService.getUserIdFromToken(refreshToken)
        tokenService.validateRefreshTokenOrThrow(userId, refreshToken)
        return userId
    }

    fun getUserIdFromAccessToken(accessToken: String): UUID {
        return jwtTokenService.getUserIdFromToken(accessToken)
    }

    fun deleteToken(userId: UUID) {
        tokenService.delete(userId)
    }
}
