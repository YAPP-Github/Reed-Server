package org.yapp.gateway.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import org.yapp.gateway.jwt.exception.JwtErrorCode
import org.yapp.gateway.jwt.exception.JwtException
import java.util.*

/**
 * Provider for JWT token generation and validation.
 */
@Component
class JwtTokenProvider(
    @Value("\${jwt.secret-key}")
    private val secretKey: String,

    @Value("\${jwt.access-token-expiration}")
    private val accessTokenExpiration: Long,

    @Value("\${jwt.refresh-token-expiration}")
    private val refreshTokenExpiration: Long
) {

    private val key = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun generateAccessToken(userId: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + accessTokenExpiration * 1000)

        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .claim("type", "access")
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    /**
     * Generate a refresh token for the given user ID.
     *
     * @param userId The ID of the user.
     * @return The generated refresh token.
     */
    fun generateRefreshToken(userId: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + refreshTokenExpiration * 1000)

        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .claim("type", "refresh")
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    /**
     * Validate a token.
     *
     * @param token The token to validate.
     * @return True if the token is valid, false otherwise.
     */
    fun validateToken(token: String): Boolean {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
            return true
        } catch (e: SignatureException) {
            // Invalid JWT signature
        } catch (e: MalformedJwtException) {
            // Invalid JWT token
        } catch (e: ExpiredJwtException) {
            // Expired JWT token
        } catch (e: UnsupportedJwtException) {
            // Unsupported JWT token
        } catch (e: IllegalArgumentException) {
            // JWT claims string is empty
        }
        return false
    }

    /**
     * Validate a token and throw an exception if it's invalid.
     *
     * @param token The token to validate.
     * @throws JwtException if the token is invalid.
     */
    fun validateTokenAndThrow(token: String) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
        } catch (e: SignatureException) {
            throw JwtException(JwtErrorCode.INVALID_JWT_SIGNATURE)
        } catch (e: MalformedJwtException) {
            throw JwtException(JwtErrorCode.INVALID_JWT_TOKEN)
        } catch (e: ExpiredJwtException) {
            throw JwtException(JwtErrorCode.EXPIRED_JWT_TOKEN)
        } catch (e: UnsupportedJwtException) {
            throw JwtException(JwtErrorCode.UNSUPPORTED_JWT_TOKEN)
        } catch (e: IllegalArgumentException) {
            throw JwtException(JwtErrorCode.EMPTY_JWT_CLAIMS)
        }
    }

    /**
     * Get the user ID from a token.
     *
     * @param token The token to extract the user ID from.
     * @return The user ID.
     * @throws JwtException if the token is invalid.
     */
    fun getUserIdFromToken(token: String): Long {
        try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body

            return claims.subject.toLong()
        } catch (e: SignatureException) {
            throw JwtException(JwtErrorCode.INVALID_JWT_SIGNATURE)
        } catch (e: MalformedJwtException) {
            throw JwtException(JwtErrorCode.INVALID_JWT_TOKEN)
        } catch (e: ExpiredJwtException) {
            throw JwtException(JwtErrorCode.EXPIRED_JWT_TOKEN)
        } catch (e: UnsupportedJwtException) {
            throw JwtException(JwtErrorCode.UNSUPPORTED_JWT_TOKEN)
        } catch (e: IllegalArgumentException) {
            throw JwtException(JwtErrorCode.EMPTY_JWT_CLAIMS)
        } catch (e: NumberFormatException) {
            throw JwtException(JwtErrorCode.INVALID_JWT_TOKEN, "Invalid user ID in token")
        }
    }

    /**
     * Get the token type from a token.
     *
     * @param token The token to extract the type from.
     * @return The token type.
     * @throws JwtException if the token is invalid or the type claim is missing.
     */
    fun getTokenType(token: String): String {
        try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body

            return claims["type"] as? String ?: throw JwtException(
                JwtErrorCode.INVALID_JWT_TOKEN,
                "Token type claim is missing"
            )
        } catch (e: SignatureException) {
            throw JwtException(JwtErrorCode.INVALID_JWT_SIGNATURE)
        } catch (e: MalformedJwtException) {
            throw JwtException(JwtErrorCode.INVALID_JWT_TOKEN)
        } catch (e: ExpiredJwtException) {
            throw JwtException(JwtErrorCode.EXPIRED_JWT_TOKEN)
        } catch (e: UnsupportedJwtException) {
            throw JwtException(JwtErrorCode.UNSUPPORTED_JWT_TOKEN)
        } catch (e: IllegalArgumentException) {
            throw JwtException(JwtErrorCode.EMPTY_JWT_CLAIMS)
        } catch (e: ClassCastException) {
            throw JwtException(JwtErrorCode.INVALID_JWT_TOKEN, "Token type claim has invalid format")
        }
    }

    /**
     * Get the authentication from a token.
     *
     * @param token The token to extract the authentication from.
     * @return The authentication.
     * @throws JwtException if the token is invalid.
     */
    fun getAuthentication(token: String): Authentication {
        // This will throw JwtException if the token is invalid
        validateTokenAndThrow(token)

        val userId = getUserIdFromToken(token)
        val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))

        return UsernamePasswordAuthenticationToken(userId, "", authorities)
    }

    /**
     * Get the claims from a token.
     *
     * @param token The token to extract the claims from.
     * @return The claims.
     * @throws JwtException if the token is invalid.
     */
    fun getClaims(token: String): Claims {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: SignatureException) {
            throw JwtException(JwtErrorCode.INVALID_JWT_SIGNATURE)
        } catch (e: MalformedJwtException) {
            throw JwtException(JwtErrorCode.INVALID_JWT_TOKEN)
        } catch (e: ExpiredJwtException) {
            throw JwtException(JwtErrorCode.EXPIRED_JWT_TOKEN)
        } catch (e: UnsupportedJwtException) {
            throw JwtException(JwtErrorCode.UNSUPPORTED_JWT_TOKEN)
        } catch (e: IllegalArgumentException) {
            throw JwtException(JwtErrorCode.EMPTY_JWT_CLAIMS)
        }
    }

    /**
     * Get the refresh token expiration time in seconds.
     *
     * @return The refresh token expiration time in seconds.
     */
    fun getRefreshTokenExpiration(): Long {
        return refreshTokenExpiration
    }
}
