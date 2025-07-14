package org.yapp.gateway.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.yapp.globalutils.auth.Role
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret-key}")
    private val secretKey: String,
    @Value("\${jwt.access-token-expiration}")
    private val accessTokenExpiration: Long,
    @Value("\${jwt.refresh-token-expiration}")
    private val refreshTokenExpiration: Long
) {
    companion object {
        private const val TOKEN_TYPE_CLAIM = "type"
        private const val ROLES_CLAIM = "roles"
        private const val ACCESS_TOKEN_TYPE = "access"
        private const val REFRESH_TOKEN_TYPE = "refresh"
        private const val MILLISECONDS_PER_SECOND = 1000L
    }

    private val key = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun generateAccessToken(userId: UUID, roles: List<Role>): String {
        val roleStrings = roles.map { it.key }
        return generateToken(userId, accessTokenExpiration, ACCESS_TOKEN_TYPE, roleStrings)
    }

    fun generateRefreshToken(userId: UUID): String {
        return generateToken(userId, refreshTokenExpiration, REFRESH_TOKEN_TYPE, emptyList())
    }

    fun getRefreshTokenExpiration(): Long {
        return refreshTokenExpiration
    }

    private fun generateToken(userId: UUID, expiration: Long, type: String, roles: List<String>): String {
        val now = Date()
        val expiryDate = Date(now.time + expiration * MILLISECONDS_PER_SECOND)

        val builder = Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .claim(TOKEN_TYPE_CLAIM, type)
            .signWith(key, SignatureAlgorithm.HS512)

        if (roles.isNotEmpty()) {
            builder.claim(ROLES_CLAIM, roles)
        }

        return builder.compact()
    }
}
