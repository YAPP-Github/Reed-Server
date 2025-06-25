package org.yapp.apis.auth.strategy

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.auth.service.AppleAuthCredentials
import org.yapp.apis.auth.service.AuthCredentials
import org.yapp.apis.util.NicknameGenerator
import org.yapp.domain.auth.ProviderType
import org.yapp.domain.user.User
import java.util.*

/**
 * Implementation of AuthStrategy for Apple authentication.
 */
@Component
class AppleAuthStrategy(
    private val objectMapper: ObjectMapper
) : AuthStrategy {

    private val log = KotlinLogging.logger {}

    companion object {
        private const val JWT_PARTS_COUNT = 3
        private const val JWT_PAYLOAD_INDEX = 1
    }

    override fun getProviderType(): ProviderType = ProviderType.APPLE

    override fun authenticate(credentials: AuthCredentials): User {
        return runCatching {
            if (credentials !is AppleAuthCredentials) {
                throw AuthException(AuthErrorCode.INVALID_CREDENTIALS, "Credentials must be AppleAuthCredentials")
            }

            val payload = parseIdToken(credentials.idToken)
            createUser(payload)
        }.getOrElse { exception ->
            log.error("Apple authentication failed", exception)
            throw when (exception) {
                is AuthException -> exception
                else -> AuthException(AuthErrorCode.FAILED_TO_GET_USER_INFO, exception.message)
            }
        }
    }

    private fun parseIdToken(idToken: String): AppleIdTokenPayload {
        return runCatching {
            val parts = idToken.split(".")
            require(parts.size == JWT_PARTS_COUNT) { "Invalid JWT format: expected $JWT_PARTS_COUNT parts but got ${parts.size}" }

            val decodedPayload = decodeBase64UrlSafe(parts[JWT_PAYLOAD_INDEX])
            val payloadJson = String(decodedPayload, Charsets.UTF_8)
            val payloadMap = objectMapper.readValue(payloadJson, Map::class.java)

            val sub = payloadMap["sub"] as? String
                ?: throw AuthException(AuthErrorCode.SUBJECT_NOT_FOUND, "Subject not found in token")

            AppleIdTokenPayload(
                sub = sub,
                email = payloadMap["email"] as? String,
                name = payloadMap["name"] as? String
            )
        }.getOrElse { exception ->
            throw when (exception) {
                is AuthException -> exception
                is IllegalArgumentException -> AuthException(
                    AuthErrorCode.INVALID_ID_TOKEN_FORMAT, "Invalid token format: ${exception.message}"
                )

                else -> AuthException(
                    AuthErrorCode.FAILED_TO_PARSE_ID_TOKEN, "Failed to parse token: ${exception.message}"
                )
            }
        }
    }

    private fun decodeBase64UrlSafe(encoded: String): ByteArray {
        return runCatching {
            Base64.getUrlDecoder().decode(encoded)
        }.getOrElse { exception ->
            throw AuthException(
                AuthErrorCode.INVALID_ID_TOKEN_FORMAT, "Invalid Base64 encoding: ${exception.message}"
            )
        }
    }

    private fun createUser(payload: AppleIdTokenPayload): User {
        return User(
            email = payload.email ?: throw AuthException(
                AuthErrorCode.EMAIL_NOT_FOUND,
                "Email not found in Apple ID token"
            ),
            nickname = NicknameGenerator.generate(),
            profileImageUrl = null, // Apple doesn't provide profile image
            providerType = ProviderType.APPLE,
            providerId = payload.sub
        )
    }

    private data class AppleIdTokenPayload(
        val sub: String,
        val email: String?,
        val name: String?
    )
}
