package org.yapp.apis.auth.usecase

import org.springframework.stereotype.Service
import org.yapp.apis.auth.dto.response.TokenPairResponse
import org.yapp.apis.auth.dto.response.UserProfileResponse
import org.yapp.apis.auth.service.AuthCredentials
import org.yapp.apis.auth.service.SocialAuthService
import org.yapp.apis.auth.service.TokenService
import org.yapp.apis.auth.service.UserService
import org.yapp.gateway.jwt.JwtTokenService
import java.util.*

@Service
class AuthUseCase(
    private val socialAuthService: SocialAuthService,
    private val userService: UserService,
    private val tokenService: TokenService,
    private val jwtTokenService: JwtTokenService
) {

    fun signIn(credentials: AuthCredentials): TokenPairResponse {
        val strategy = socialAuthService.resolve(credentials)
        val userInfo = strategy.authenticate(credentials)

        val user = userService.findOrCreateUser(userInfo)
        return generateTokenPair(user.id!!)
    }

    fun refreshToken(refreshToken: String): TokenPairResponse {
        val userId = jwtTokenService.getUserIdFromToken(refreshToken)
        tokenService.validateRefreshTokenOrThrow(userId, refreshToken)
        return generateTokenPair(userId)
    }

    fun signOut(userId: UUID) {
        tokenService.delete(userId)
    }

    fun getUserProfile(userId: UUID): UserProfileResponse {
        val user = userService.findUserById(userId)
        return UserProfileResponse.of(
            id = user.id!!,
            email = user.email,
            nickname = user.nickname,
            provider = user.providerType
        )
    }

    fun getUserIdFromAccessToken(accessToken: String): UUID {
        return jwtTokenService.getUserIdFromToken(accessToken)
    }

    private fun generateTokenPair(userId: UUID): TokenPairResponse {
        val accessToken = jwtTokenService.generateAccessToken(userId)
        val refreshToken = jwtTokenService.generateRefreshToken(userId)
        val expiration = jwtTokenService.getRefreshTokenExpiration()

        tokenService.save(userId, refreshToken, expiration)
        return TokenPairResponse.of(accessToken, refreshToken)
    }
}
