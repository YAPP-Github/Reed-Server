package org.yapp.apis.auth.helper.apple

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.yapp.apis.config.AppleOauthProperties
import org.yapp.globalutils.annotation.Helper
import java.time.Instant
import java.time.temporal.ChronoUnit

@Helper
class AppleClientSecretGenerator(
    private val appleProperties: AppleOauthProperties,
    @Qualifier("appleJwtEncoder")
    private val jwtEncoder: JwtEncoder,
) {
    fun generateClientSecret(): String {
        val header = JwsHeader.with(SignatureAlgorithm.ES256)
            .keyId(appleProperties.keyId)
            .build()

        val claims = JwtClaimsSet.builder()
            .issuer(appleProperties.teamId)
            .subject(appleProperties.clientId)
            .audience(listOf(appleProperties.audience))
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plus(5, ChronoUnit.MINUTES))
            .build()

        val encoderParameters = JwtEncoderParameters.from(header, claims)

        return jwtEncoder.encode(encoderParameters).tokenValue
    }
}
