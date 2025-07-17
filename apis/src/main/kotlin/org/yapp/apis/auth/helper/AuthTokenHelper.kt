package org.yapp.apis.auth.helper

import org.yapp.apis.auth.dto.request.DeleteTokenRequest
import org.yapp.apis.auth.dto.request.GenerateTokenPairRequest
import org.yapp.apis.auth.dto.request.TokenGenerateRequest
import org.yapp.apis.auth.dto.request.TokenRefreshRequest
import org.yapp.apis.auth.dto.response.TokenPairResponse
import org.yapp.apis.auth.dto.response.UserIdResponse
import org.yapp.apis.auth.service.TokenService
import org.yapp.gateway.jwt.JwtTokenService
import org.yapp.globalutils.annotation.Helper

@Helper
class AuthTokenHelper(
    private val tokenService: TokenService,
    private val jwtTokenService: JwtTokenService
) {
    fun generateTokenPair(generateTokenPairRequest: GenerateTokenPairRequest): TokenPairResponse {
        val userId = generateTokenPairRequest.validUserId()
        val role = generateTokenPairRequest.validRole()

        val accessToken = jwtTokenService.generateAccessToken(userId, role)
        val refreshToken = jwtTokenService.generateRefreshToken(userId)
        val expiration = jwtTokenService.getRefreshTokenExpiration()

        val refreshTokenResponse = tokenService.saveRefreshToken(
            TokenGenerateRequest.of(userId, refreshToken, expiration)
        )

        return TokenPairResponse.of(accessToken, refreshTokenResponse.refreshToken)
    }

    fun validateAndGetUserIdFromRefreshToken(tokenRefreshRequest: TokenRefreshRequest): UserIdResponse {
        tokenService.validateRefreshToken(tokenRefreshRequest.validRefreshToken())
        return tokenService.getUserIdByToken(tokenRefreshRequest)
    }

    fun deleteTokenForReissue(tokenRefreshRequest: TokenRefreshRequest) {
        tokenService.deleteRefreshTokenByToken(tokenRefreshRequest.validRefreshToken())
    }

    fun deleteTokenForSignOut(deleteTokenRequest: DeleteTokenRequest) {
        tokenService.deleteRefreshTokenByToken(deleteTokenRequest.validRefreshToken())
    }
}
