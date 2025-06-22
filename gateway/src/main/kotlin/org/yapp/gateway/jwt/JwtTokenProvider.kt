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

    fun getUserIdFromToken(token: String): Long {
        val claims = parseToken(token)
        return try {
            claims.subject.toLong()
        } catch (e: NumberFormatException) {
            throw JwtException(JwtErrorCode.INVALID_JWT_TOKEN, "Invalid user ID in token")
        }
    }

    fun getTokenType(token: String): String {
        val claims = parseToken(token)
        return claims["type"] as? String
            ?: throw JwtException(JwtErrorCode.INVALID_JWT_TOKEN, "Token type claim is missing")
    }

    fun getAuthentication(token: String): Authentication {
        validateTokenAndThrow(token)
        val userId = getUserIdFromToken(token)
        val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
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
}
