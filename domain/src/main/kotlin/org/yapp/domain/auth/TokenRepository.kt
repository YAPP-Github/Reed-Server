package org.yapp.domain.auth

import java.util.*

/**
 * Repository interface for token storage.
 */
interface TokenRepository {

    fun saveRefreshToken(userId: UUID, refreshToken: String, expirationTimeInSeconds: Long)

    fun getRefreshToken(userId: UUID): String?

    fun deleteRefreshToken(userId: UUID)

    fun existsRefreshToken(userId: UUID, refreshToken: String): Boolean
}
