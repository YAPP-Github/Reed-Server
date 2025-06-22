package org.yapp.apis.auth.service

import org.springframework.stereotype.Service
import org.yapp.apis.auth.dto.TokenPair
import org.yapp.apis.auth.dto.UserProfileResponse
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.auth.strategy.AuthStrategy
import org.yapp.domain.auth.TokenRepository
import org.yapp.domain.user.User
import org.yapp.domain.user.UserRepository
import org.yapp.gateway.jwt.JwtTokenService
import org.yapp.gateway.jwt.exception.JwtException

/**
 * Implementation of AuthService.
 */
@Service
class AuthServiceImpl(
    private val authStrategies: List<AuthStrategy>,
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val jwtTokenService: JwtTokenService
) : AuthService {

    override fun signIn(credentials: AuthCredentials): TokenPair {
        val strategy = getAuthStrategy(credentials)
        val userInfo = strategy.authenticate(credentials)

        val existingUser = userRepository.findByProviderTypeAndProviderId(
            userInfo.providerType,
            userInfo.providerId
        )

        if (existingUser != null) {
            return generateTokenPair(
                existingUser.id ?: throw IllegalStateException("Existing user id should not be null")
            )
        }

        userRepository.findByEmail(userInfo.email)?.let {
            throw AuthException(AuthErrorCode.EMAIL_ALREADY_IN_USE)
        }

        // User doesn't exist, so create a new one
        val savedUser = userRepository.save(userInfo)

        return generateTokenPair(savedUser.id!!)
    }

    override fun signUp(credentials: AuthCredentials): TokenPair = signIn(credentials)

    override fun refreshToken(refreshToken: String): TokenPair {
        if (!jwtTokenService.validateToken(refreshToken)) {
            throw AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN)
        }

        val userId: Long
        try {
            userId = jwtTokenService.getUserIdFromToken(refreshToken)
        } catch (e: JwtException) {
            throw AuthException(AuthErrorCode.INVALID_REFRESH_TOKEN, e.message ?: "Invalid refresh token")
        }

        if (!tokenRepository.existsRefreshToken(userId, refreshToken)) {
            throw AuthException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND)
        }

        return generateTokenPair(userId)
    }

    override fun signOut(userId: Long) {
        tokenRepository.deleteRefreshToken(userId)
    }

    override fun getUserById(userId: Long): User? = userRepository.findById(userId)

    override fun getUserProfile(userId: Long): UserProfileResponse {
        val user = userRepository.findById(userId)
        return UserProfileResponse(
            id = user.id!!,
            email = user.email,
            nickname = user.nickname,
            provider = user.providerType.name
        )
    }

    override fun getUserIdFromAccessToken(accessToken: String): Long {
        if (!jwtTokenService.validateToken(accessToken)) {
            throw AuthException(AuthErrorCode.INVALID_ACCESS_TOKEN)
        }

        try {
            return jwtTokenService.getUserIdFromToken(accessToken)
        } catch (e: JwtException) {
            throw AuthException(AuthErrorCode.INVALID_ACCESS_TOKEN, e.message ?: "Invalid access token")
        }
    }


    private fun getAuthStrategy(credentials: AuthCredentials): AuthStrategy {
        return authStrategies.find { it.getProviderType() == credentials.getProviderType() }
            ?: throw AuthException(
                AuthErrorCode.UNSUPPORTED_PROVIDER_TYPE,
                "Unsupported provider type: ${credentials.getProviderType()}"
            )
    }

    private fun generateTokenPair(userId: Long): TokenPair {
        val accessToken = jwtTokenService.generateAccessToken(userId)
        val refreshToken = jwtTokenService.generateRefreshToken(userId)

        val refreshTokenExpiration = jwtTokenService.getRefreshTokenExpiration()
        tokenRepository.saveRefreshToken(userId, refreshToken, refreshTokenExpiration)

        return TokenPair(accessToken, refreshToken)
    }
}
