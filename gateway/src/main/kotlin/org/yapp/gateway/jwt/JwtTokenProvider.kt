package org.yapp.gateway.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import org.yapp.gateway.jwt.exception.JwtErrorCode
import org.yapp.gateway.jwt.exception.JwtException as CustomJwtException

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
        private const val ACCESS_TOKEN_TYPE = "access"
        private const val REFRESH_TOKEN_TYPE = "refresh"
        private const val DEFAULT_ROLE = "ROLE_USER"
        private const val MILLISECONDS_PER_SECOND = 1000L
    }

    private val key = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun generateAccessToken(userId: UUID): String {
        return generateToken(userId, accessTokenExpiration, ACCESS_TOKEN_TYPE)
    }

    fun generateRefreshToken(userId: UUID): String {
        return generateToken(userId, refreshTokenExpiration, REFRESH_TOKEN_TYPE)
    }

    fun validateToken(token: String): Boolean {
        return try {
            parseToken(token)
            true
        } catch (e: JwtException) {
            false
        }
    }

    fun validateTokenAndThrow(token: String) {
        parseToken(token) // 예외는 내부에서 throw됨
    }

    fun getUserIdFromToken(token: String): UUID {
        val claims = parseToken(token)
        return try {
            claims.subject.let { UUID.fromString(it) }
        } catch (e: NumberFormatException) {
            throw CustomJwtException(
                JwtErrorCode.INVALID_JWT_TOKEN,
                "Invalid user ID in token"
            )
        }
    }

    fun getTokenType(token: String): String {
        val claims = parseToken(token)
        return when (val type = claims[TOKEN_TYPE_CLAIM]) {
            is String -> type
            null -> throw CustomJwtException(
                JwtErrorCode.INVALID_JWT_TOKEN,
                "Token type claim is missing"
            )
            else -> throw CustomJwtException(
                JwtErrorCode.INVALID_JWT_TOKEN,
                "Token type claim has invalid type: ${type::class.simpleName}"
            )
        }
    }

    fun getAuthentication(token: String): Authentication {
        validateTokenAndThrow(token)
        val userId = getUserIdFromToken(token)
        val authorities = listOf(SimpleGrantedAuthority(DEFAULT_ROLE))
        return UsernamePasswordAuthenticationToken(userId, "", authorities)
    }

    fun getClaims(token: String): Claims = parseToken(token)

    fun getRefreshTokenExpiration(): Long = refreshTokenExpiration

    /**
     * 공통 JWT 파싱 로직. 실패 시 JwtException 던짐.
     */
    private fun parseToken(token: String): Claims {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: SecurityException) {
            throw CustomJwtException(JwtErrorCode.INVALID_JWT_SIGNATURE)
        } catch (e: MalformedJwtException) {
            throw CustomJwtException(JwtErrorCode.INVALID_JWT_TOKEN)
        } catch (e: ExpiredJwtException) {
            throw CustomJwtException(JwtErrorCode.EXPIRED_JWT_TOKEN)
        } catch (e: UnsupportedJwtException) {
            throw CustomJwtException(JwtErrorCode.UNSUPPORTED_JWT_TOKEN)
        } catch (e: IllegalArgumentException) {
            throw CustomJwtException(JwtErrorCode.EMPTY_JWT_CLAIMS)
        }
    }

    private fun generateToken(userId: UUID, expiration: Long, type: String): String {
        val now = Date()
        val expiryDate = Date(now.time + expiration * MILLISECONDS_PER_SECOND)

        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .claim("type", type)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }
}
