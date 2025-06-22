package org.yapp.gateway.jwt

import org.yapp.gateway.jwt.exception.JwtException
import java.util.*

/**
 * Interface for JWT token operations.
 */
interface JwtTokenService {

    /**
     * Generate an access token for the given user ID.
     *
     * @param userId The ID of the user.
     * @return The generated access token.
     */
    fun generateAccessToken(userId: UUID): String

    /**
     * Generate a refresh token for the given user ID.
     *
     * @param userId The ID of the user.
     * @return The generated refresh token.
     */
    fun generateRefreshToken(userId: UUID): String

    /**
     * Validate a token.
     *
     * @param token The token to validate.
     * @return True if the token is valid, false otherwise.
     */
    fun validateToken(token: String): Boolean

    /**
     * Get the user ID from a token.
     *
     * @param token The token to extract the user ID from.
     * @return The user ID.
     * @throws JwtException if the token is invalid.
     */
    fun getUserIdFromToken(token: String): UUID

    /**
     * Get the refresh token expiration time in seconds.
     *
     * @return The refresh token expiration time in seconds.
     */
    fun getRefreshTokenExpiration(): Long
}
