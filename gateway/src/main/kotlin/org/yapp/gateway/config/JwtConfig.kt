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

    /**
     * JWT를 암호화하고 서명하는 데 사용될 키의 소스(`JWKSource`)를 생성하여 Bean으로 등록합니다.
     * HMAC-SHA 알고리즘에 사용될 대칭 키를 `OctetSequenceKey` 형태로 래핑하여 제공합니다.
     *
     * @return 생성된 `JWKSource<SecurityContext>` 객체
     */
    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {
        val jwk: OctetSequenceKey = OctetSequenceKey.Builder(secretKey.toByteArray()).build()
        return ImmutableJWKSet(JWKSet(jwk))
    }

    /**
     * JWT를 생성(인코딩)하는 `JwtEncoder`를 생성하여 Bean으로 등록합니다.
     * 주입받은 `JWKSource`를 사용하여 토큰에 디지털 서명을 수행합니다.
     *
     * @param jwkSource 토큰 서명에 사용될 키 소스
     * @return 생성된 `JwtEncoder` 객체
     */
    @Bean
    fun jwtEncoder(jwkSource: JWKSource<SecurityContext>): JwtEncoder {
        return NimbusJwtEncoder(jwkSource)
    }

    /**
     * 클라이언트로부터 받은 JWT를 검증(디코딩)하는 `JwtDecoder`를 생성하여 Bean으로 등록합니다.
     * 토큰의 서명, 만료 시간(기본 검증) 및 발급자(issuer)가 일치하는지 확인합니다.
     *
     * @return 생성된 `JwtDecoder` 객체
     */
    @Bean
    fun jwtDecoder(): JwtDecoder {
        val secretKeySpec = SecretKeySpec(secretKey.toByteArray(), SIGNATURE_ALGORITHM.name)
        val decoder = NimbusJwtDecoder.withSecretKey(secretKeySpec).build()
        val validator = JwtValidators.createDefaultWithIssuer(JwtConstants.ISSUER)
        decoder.setJwtValidator(validator)
        return decoder
    }

    /**
     * 유효성이 검증된 JWT를 Spring Security의 `Authentication` 객체로 변환하는 `JwtAuthenticationConverter`를 등록합니다.
     * JWT의 'roles' 클레임을 애플리케이션의 권한 정보(`GrantedAuthority`)로 매핑하고, 'sub' 클레임을 사용자의 주체(Principal)로 설정합니다.
     *
     * @return 생성된 `JwtAuthenticationConverter` 객체
     */
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
