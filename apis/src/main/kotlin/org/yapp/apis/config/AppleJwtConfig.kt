package org.yapp.apis.config

import com.nimbusds.jose.jwk.Curve
import com.nimbusds.jose.jwk.ECKey
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.core.*
import org.springframework.security.oauth2.jwt.*
import org.yapp.apis.auth.helper.apple.ApplePrivateKeyLoader
import java.security.interfaces.ECPublicKey

@Configuration
class AppleJwtConfig(
    private val applePrivateKeyLoader: ApplePrivateKeyLoader,
    private val appleProperties: AppleOauthProperties
) {
    companion object {
        private const val APPLE_JWKS_URI = "https://appleid.apple.com/auth/keys"
    }

    /**
     * Apple OAuth 인증을 위한 JWKSource를 생성합니다.
     *
     * - EC 키 쌍(KeyPair)을 기반으로 JWK(JSON Web Key)를 구성
     * - Nimbus 라이브러리의 JwtEncoder에서 Apple client_secret 생성 시 사용
     *
     * @return Apple 인증용 JWKSource<SecurityContext> (ImmutableJWKSet 형태)
     */
    @Bean
    @Qualifier("appleJwkSource")
    fun appleJwkSource(): JWKSource<SecurityContext> {
        val keyPair = applePrivateKeyLoader.keyPair
        val publicKey = keyPair.public as ECPublicKey
        val privateKey = keyPair.private

        val ecKey = ECKey.Builder(Curve.P_256, publicKey)
            .privateKey(privateKey)
            .keyID(appleProperties.keyId)
            .build()

        return ImmutableJWKSet(JWKSet(ecKey))
    }

    /**
     * Apple 전용 JWK 소스를 사용하여 client_secret 생성에 사용될 JwtEncoder를 생성합니다.
     *
     * - NimbusJwtEncoder는 JWKSource 기반으로 JWT 서명을 처리
     *
     * @param jwkSource Apple용 JWKSource Bean
     * @return Apple client_secret 생성용 JwtEncoder 객체
     */
    @Bean
    @Qualifier("appleJwtEncoder")
    fun appleJwtEncoder(
        @Qualifier("appleJwkSource") jwkSource: JWKSource<SecurityContext>
    ): JwtEncoder {
        return NimbusJwtEncoder(jwkSource)
    }

    /**
     * Apple에서 발급한 ID Token을 검증하기 위한 JwtDecoder를 생성합니다.
     *
     * - NimbusJwtDecoder를 사용하여 Apple 공개키(JWKS) 기반 검증 수행
     * - APPLE_JWKS_URI("https://appleid.apple.com/auth/keys")에서 공개키(JWK Set) 가져오기
     *
     *  검증 로직
     *   1. 서명(Signature) 유효성 검증: Apple의 공개키와 토큰 서명이 일치하는지 확인
     *   2. Issuer 검증: 토큰의 iss 클레임이 예상 값인지 확인
     *   3. Audience 검증: 토큰의 aud 클레임이 애플리케이션의 clientId와 일치하는지 확인
     *
     * @return Apple ID Token 검증용 JwtDecoder
     */
    @Bean
    @Qualifier("appleIdTokenDecoder")
    fun appleIdTokenDecoder(): JwtDecoder {
        val decoder = NimbusJwtDecoder.withJwkSetUri(APPLE_JWKS_URI).build()

        val issuerValidator: OAuth2TokenValidator<Jwt> =
            JwtValidators.createDefaultWithIssuer(appleProperties.audience)

        val audienceValidator = OAuth2TokenValidator<Jwt> { token ->
            if (token.audience.contains(appleProperties.clientId)) {
                OAuth2TokenValidatorResult.success()
            } else {
                val error = OAuth2Error(
                    OAuth2ErrorCodes.INVALID_TOKEN, "The required audience is missing", null
                )
                OAuth2TokenValidatorResult.failure(error)
            }
        }

        val combinedValidator = DelegatingOAuth2TokenValidator(issuerValidator, audienceValidator)
        decoder.setJwtValidator(combinedValidator)

        return decoder
    }
}
