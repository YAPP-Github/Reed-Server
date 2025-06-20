package org.yapp.apis.auth.usecase

import org.springframework.stereotype.Service
import org.yapp.apis.auth.dto.UserProfileResponse
import org.yapp.apis.auth.service.AuthCredentials
import org.yapp.apis.auth.service.AuthService
import org.yapp.apis.auth.service.TokenPair

/**
 * Implementation of AuthUseCase that uses AuthService.
 */
@Service
class AuthUseCaseImpl(
    private val authService: AuthService
) : AuthUseCase {

    override fun signIn(credentials: AuthCredentials): TokenPair {
        return authService.signIn(credentials)
    }

    override fun signUp(credentials: AuthCredentials): TokenPair {
        return authService.signUp(credentials)
    }

    override fun refreshToken(refreshToken: String): TokenPair {
        return authService.refreshToken(refreshToken)
    }

    override fun signOut(userId: Long) {
        authService.signOut(userId)
    }

    override fun getUserProfile(userId: Long): UserProfileResponse {
        return authService.getUserProfile(userId)
    }

    override fun getUserIdFromAccessToken(accessToken: String): Long {
        return authService.getUserIdFromAccessToken(accessToken)
    }
}