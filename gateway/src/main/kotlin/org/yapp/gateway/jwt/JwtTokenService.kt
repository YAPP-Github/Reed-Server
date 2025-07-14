package org.yapp.gateway.jwt

import org.yapp.globalutils.auth.Role
import java.util.*

/**
 * Interface for JWT token operations.
 */
interface JwtTokenService {

    /**
     * Generate an access token for the given user ID.
     *
     * @param userId The ID of the user.
     * @param role The Role of the user.
     * @return The generated access token.
     */
    fun generateAccessToken(userId: UUID, role: Role): String

    /**
     * Generate a refresh token for the given user ID.
     *
     * @param userId The ID of the user.
     * @return The generated refresh token.
     */
    fun generateRefreshToken(userId: UUID): String

    /**
     * Get the refresh token expiration time in seconds.
     *
     * @return The refresh token expiration time in seconds.
     */
    fun getRefreshTokenExpiration(): Long
}
