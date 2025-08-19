package org.yapp.gateway.config

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.OctetSequenceKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.*
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.yapp.gateway.constants.JwtConstants
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class JwtConfig(
    private val jwtProperties: JwtProperties
) {
    companion object {
        private val SIGNATURE_ALGORITHM = MacAlgorithm.HS256
        private const val PRINCIPAL_CLAIM = "sub"
        private const val NO_AUTHORITY_PREFIX = ""
    }

    /**
     * JWT를 암호화하고 서명하는 데 사용될 키의 소스(`JWKSource`)를 생성하여 Bean으로 등록합니다.
     * HMAC-SHA 알고리즘에 사용될 대칭 키를 `OctetSequenceKey` 형태로 래핑하여 제공합니다.
     *
     * @return 생성된 `JWKSource<SecurityContext>` 객체
     */
    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {
        val jwk: OctetSequenceKey = OctetSequenceKey.Builder(jwtProperties.secretKey.toByteArray()).build()
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
    @Primary
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
    @Primary
    fun jwtDecoder(): JwtDecoder {
        val secretKeySpec = SecretKeySpec(jwtProperties.secretKey.toByteArray(), SIGNATURE_ALGORITHM.name)
        val decoder = NimbusJwtDecoder.withSecretKey(secretKeySpec).build()
        val validator = JwtValidators.createDefaultWithIssuer(JwtConstants.ISSUER)
        decoder.setJwtValidator(validator)
        return decoder
    }

    /**
     * 유효한 JWT를 Spring Security의 `Authentication` 객체로 변환하는 `JwtAuthenticationConverter` 빈을 설정합니다.
     *
     * - JWT의 `roles` 클레임을 추출하여 `GrantedAuthority` 리스트로 변환합니다.
     * - 기본적으로 붙는 권한 접두어(`SCOPE_`)를 제거하기 위해 빈 문자열로 설정합니다.
     * - JWT의 `sub` 클레임(문자열 형태의 UUID)을 `UUID` 타입으로 변환하여 인증 주체(Principal)로 사용합니다.
     *
     * @return JWT를 `UsernamePasswordAuthenticationToken` 으로 변환하는 Converter
     */
    @Bean
    fun jwtAuthenticationConverter(): Converter<Jwt, out AbstractAuthenticationToken> {
        val authoritiesConverter = JwtGrantedAuthoritiesConverter()
        authoritiesConverter.setAuthoritiesClaimName(JwtConstants.ROLES_CLAIM)
        authoritiesConverter.setAuthorityPrefix(NO_AUTHORITY_PREFIX)

        return Converter<Jwt, AbstractAuthenticationToken> { jwt ->
            val authorities = authoritiesConverter.convert(jwt)
            val principal = UUID.fromString(jwt.getClaimAsString(PRINCIPAL_CLAIM))
            UsernamePasswordAuthenticationToken(principal, null, authorities)
        }
    }
}
