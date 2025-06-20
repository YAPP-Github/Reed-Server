package org.yapp.apis.auth.usecase

import org.yapp.apis.auth.dto.UserProfileResponse
import org.yapp.apis.auth.service.AuthCredentials
import org.yapp.apis.auth.service.TokenPair

/**
 * Interface for authentication use cases.
 */
interface AuthUseCase {

    fun signIn(credentials: AuthCredentials): TokenPair

    fun signUp(credentials: AuthCredentials): TokenPair

    fun refreshToken(refreshToken: String): TokenPair

    fun signOut(userId: Long)

    fun getUserProfile(userId: Long): UserProfileResponse

    fun getUserIdFromAccessToken(accessToken: String): Long
}