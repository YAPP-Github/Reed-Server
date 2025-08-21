package org.yapp.apis.auth.helper.apple

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.globalutils.annotation.Helper

@Helper
class AppleIdTokenProcessor(
    @Qualifier("appleIdTokenDecoder")
    private val jwtDecoder: JwtDecoder,
    private val objectMapper: ObjectMapper
) {
    fun parseAndValidate(idToken: String): AppleIdTokenPayload {
        try {
            val decodedJwt: Jwt = jwtDecoder.decode(idToken)
            val claims = decodedJwt.claims

            return objectMapper.convertValue(claims, AppleIdTokenPayload::class.java)
        } catch (e: Exception) {
            throw AuthException(AuthErrorCode.INVALID_APPLE_ID_TOKEN, e.message)
        }
    }

    data class AppleIdTokenPayload(
        val sub: String,
        val email: String?
    )
}
