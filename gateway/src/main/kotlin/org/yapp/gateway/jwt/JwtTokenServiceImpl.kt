package org.yapp.gateway.jwt

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.yapp.gateway.jwt.exception.JwtException
import java.util.*

/**
 * Implementation of JwtTokenService using JwtTokenProvider.
 */
@Service
class JwtTokenServiceImpl(
    private val jwtTokenProvider: JwtTokenProvider,
) : JwtTokenService {

    /**
     * Generate an access token for the given user ID.
     *
     * @param userId The ID of the user.
     * @return The generated access token.
     */
    override fun generateAccessToken(userId: UUID): String {
        return jwtTokenProvider.generateAccessToken(userId)
    }

    /**
     * Generate a refresh token for the given user ID.
     *
     * @param userId The ID of the user.
     * @return The generated refresh token.
     */
    override fun generateRefreshToken(userId: UUID): String {
        return jwtTokenProvider.generateRefreshToken(userId)
    }

    /**
     * Validate a token.
     *
     * @param token The token to validate.
     * @return True if the token is valid, false otherwise.
     */
    override fun validateToken(token: String): Boolean {
        return jwtTokenProvider.validateToken(token)
    }

    /**
     * Get the user ID from a token.
     *
     * @param token The token to extract the user ID from.
     * @return The user ID.
     * @throws JwtException if the token is invalid.
     */
    override fun getUserIdFromToken(token: String): UUID {
        return jwtTokenProvider.getUserIdFromToken(token)
    }

    /**
     * Get the refresh token expiration time in seconds.
     *
     * @return The refresh token expiration time in seconds.
     */
    override fun getRefreshTokenExpiration(): Long {
        return jwtTokenProvider.getRefreshTokenExpiration();
    }
}
