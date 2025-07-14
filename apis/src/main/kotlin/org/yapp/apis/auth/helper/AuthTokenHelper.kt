package org.yapp.apis.auth.helper

import org.yapp.apis.auth.dto.response.TokenPairResponse
import org.yapp.apis.auth.service.TokenService
import org.yapp.gateway.jwt.JwtTokenService
import org.yapp.globalutils.annotation.Helper
import org.yapp.globalutils.auth.Role
import java.util.*

@Helper
class AuthTokenHelper(
    private val tokenService: TokenService,
    private val jwtTokenService: JwtTokenService
) {
    fun generateTokenPair(userId: UUID, role: Role): TokenPairResponse {
        val accessToken = jwtTokenService.generateAccessToken(userId, role)
        val refreshToken = jwtTokenService.generateRefreshToken(userId)
        val expiration = jwtTokenService.getRefreshTokenExpiration()

        tokenService.save(userId, refreshToken, expiration)
        return TokenPairResponse.of(accessToken, refreshToken)
    }

    fun validateAndGetUserIdFromRefreshToken(refreshToken: String): UUID {
        tokenService.validateRefreshTokenByTokenOrThrow(refreshToken)
        return tokenService.getUserIdFromToken(refreshToken)
    }

    fun deleteToken(token: String) {
        tokenService.deleteByToken(token)
    }
}
