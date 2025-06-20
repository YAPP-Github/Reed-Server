package org.yapp.apis.auth.service

import org.yapp.apis.auth.dto.UserProfileResponse
import org.yapp.domain.user.User

/**
 * Interface for authentication services.
 */
interface AuthService {

    fun signIn(credentials: AuthCredentials): TokenPair

    fun signUp(credentials: AuthCredentials): TokenPair

    fun refreshToken(refreshToken: String): TokenPair

    fun signOut(userId: Long)

    fun getUserById(userId: Long): User?

    fun getUserProfile(userId: Long): UserProfileResponse

    fun getUserIdFromAccessToken(accessToken: String): Long

}


data class TokenPair(
    val accessToken: String,
    val refreshToken: String
)