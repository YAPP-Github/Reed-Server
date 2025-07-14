package org.yapp.gateway.jwt

import org.springframework.stereotype.Service
import org.yapp.globalutils.auth.Role
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
     * @param role The Role of the user.
     * @return The generated access token.
     */
    override fun generateAccessToken(userId: UUID, role: Role): String {
        return jwtTokenProvider.generateAccessToken(userId, listOf(role))
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
     * Get the refresh token expiration time in seconds.
     *
     * @return The refresh token expiration time in seconds.
     */
    override fun getRefreshTokenExpiration(): Long {
        return jwtTokenProvider.getRefreshTokenExpiration()
    }
}
