package org.yapp.gateway.config

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.OctetSequenceKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.*
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.yapp.gateway.constants.JwtConstants
import javax.crypto.spec.SecretKeySpec

@Configuration
class JwtConfig(
    @Value("\${jwt.secret-key}")
    private val secretKey: String
) {
    companion object {
        private val SIGNATURE_ALGORITHM = MacAlgorithm.HS256
        private const val PRINCIPAL_CLAIM = "sub"
    }

    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {
        val jwk: OctetSequenceKey = OctetSequenceKey.Builder(secretKey.toByteArray()).build()
        return ImmutableJWKSet(JWKSet(jwk))
    }

    @Bean
    fun jwtEncoder(jwkSource: JWKSource<SecurityContext>): JwtEncoder {
        return NimbusJwtEncoder(jwkSource)
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        val secretKeySpec = SecretKeySpec(secretKey.toByteArray(), SIGNATURE_ALGORITHM.getName())
        val decoder = NimbusJwtDecoder.withSecretKey(secretKeySpec).build()
        val validator = JwtValidators.createDefaultWithIssuer(JwtConstants.ISSUER)
        decoder.setJwtValidator(validator)
        return decoder
    }

    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val converter = JwtAuthenticationConverter()
        converter.setJwtGrantedAuthoritiesConverter { jwt ->
            val roles = jwt.getClaimAsStringList(JwtConstants.ROLES_CLAIM) ?: emptyList()
            roles.map { role -> SimpleGrantedAuthority(role) }
        }
        converter.setPrincipalClaimName(PRINCIPAL_CLAIM)
        return converter
    }
}
