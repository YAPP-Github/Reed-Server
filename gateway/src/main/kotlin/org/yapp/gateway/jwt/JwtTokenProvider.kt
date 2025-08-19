package org.yapp.gateway.jwt

import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Component
import org.yapp.gateway.config.JwtProperties
import org.yapp.gateway.constants.JwtConstants
import org.yapp.globalutils.auth.Role
import java.time.Duration
import java.time.Instant
import java.util.*

@Component
class JwtTokenProvider(
    private val jwtEncoder: JwtEncoder,
    private val jwtProperties: JwtProperties
) {
    companion object {
        private const val TOKEN_TYPE_CLAIM = "type"
        private const val ACCESS_TOKEN_TYPE = "access"
        private const val REFRESH_TOKEN_TYPE = "refresh"
    }

    fun generateAccessToken(userId: UUID, roles: List<Role>): String {
        val roleStrings = roles.map { it.key }
        val claims = mapOf(
            JwtConstants.ROLES_CLAIM to roleStrings,
            TOKEN_TYPE_CLAIM to ACCESS_TOKEN_TYPE
        )
        return generateToken(userId.toString(), jwtProperties.accessTokenExpiration, claims)
    }

    fun generateRefreshToken(userId: UUID): String {
        val claims = mapOf(TOKEN_TYPE_CLAIM to REFRESH_TOKEN_TYPE)
        return generateToken(userId.toString(), jwtProperties.refreshTokenExpiration, claims)
    }

    fun getRefreshTokenExpiration(): Long {
        return jwtProperties.refreshTokenExpiration.toSeconds()
    }

    private fun generateToken(
        subject: String,
        expiration: Duration,
        claims: Map<String, Any>
    ): String {
        val now = Instant.now()
        val expiry = now.plus(expiration)

        val claimsSet = JwtClaimsSet.builder()
            .issuer(JwtConstants.ISSUER)
            .issuedAt(now)
            .expiresAt(expiry)
            .subject(subject)
            .claims { it.putAll(claims) }
            .build()

        val header = JwsHeader.with(MacAlgorithm.HS256).build()
        val encoderParameters = JwtEncoderParameters.from(header, claimsSet)
        return jwtEncoder.encode(encoderParameters).tokenValue
    }
}
