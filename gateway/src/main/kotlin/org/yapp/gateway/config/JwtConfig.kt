package org.yapp.gateway.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import javax.crypto.spec.SecretKeySpec

@Configuration
class JwtConfig(
    @Value("\${jwt.secret-key}")
    private val secretKey: String
) {
    companion object {
        private const val SIGNATURE_ALGORITHM = "HmacSHA256"
        private const val ROLES_CLAIM = "roles"
        private const val PRINCIPAL_CLAIM = "sub"
    }

    /**
     * JWT를 디코딩하고 서명을 검증하는 JwtDecoder Bean을 생성합니다.
     * Spring Security의 oauth2ResourceServer는 이 Bean을 사용하여 토큰의 유효성을 자동으로 검사합니다.
     * (만료, 서명 불일치 등 모든 검증을 여기서 처리합니다)
     */
    @Bean
    fun jwtDecoder(): JwtDecoder {
        val secretKeySpec = SecretKeySpec(secretKey.toByteArray(), SIGNATURE_ALGORITHM)
        return NimbusJwtDecoder.withSecretKey(secretKeySpec).build()
    }

    /**
     * 유효성이 검증된 JWT를 Authentication 객체로 변환하는 Converter Bean을 생성합니다.
     * 이를 통해 JWT의 claims를 애플리케이션의 권한(GrantedAuthority) 정보로 매핑할 수 있습니다.
     */
    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val converter = JwtAuthenticationConverter()

        converter.setJwtGrantedAuthoritiesConverter { jwt ->
            val roles = jwt.getClaimAsStringList(ROLES_CLAIM) ?: emptyList()
            roles.map { role -> SimpleGrantedAuthority(role) }
        }

        converter.setPrincipalClaimName(PRINCIPAL_CLAIM)

        return converter
    }
}
