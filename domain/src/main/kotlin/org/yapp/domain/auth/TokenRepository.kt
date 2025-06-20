package org.yapp.domain.auth

/**
 * Repository interface for token storage.
 */
interface TokenRepository {
    
    fun saveRefreshToken(userId: Long, refreshToken: String, expirationTimeInSeconds: Long)

    fun getRefreshToken(userId: Long): String?

    fun deleteRefreshToken(userId: Long)

    fun existsRefreshToken(userId: Long, refreshToken: String): Boolean
}