package org.yapp.apis.auth.strategy

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.yapp.apis.auth.dto.AppleAuthCredentials
import org.yapp.apis.auth.dto.AuthCredentials
import org.yapp.apis.auth.dto.UserCreateInfo
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.util.NicknameGenerator
import org.yapp.domain.auth.ProviderType
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

    override fun authenticate(credentials: AuthCredentials): UserCreateInfo {
        return try {
            val appleCredentials = credentials as AppleAuthCredentials
            val payload = parseIdToken(appleCredentials.idToken)
            createUserInfo(payload)
        } catch (exception: Exception) {
            log.error("Apple authentication failed", exception)
            when (exception) {
                is AuthException -> throw exception
                is ClassCastException -> throw AuthException(
                    AuthErrorCode.INVALID_CREDENTIALS,
                    "Credentials must be AppleAuthCredentials"
                )

                else -> throw AuthException(AuthErrorCode.FAILED_TO_GET_USER_INFO, exception.message)
            }
        }
    }

    private fun parseIdToken(idToken: String): AppleIdTokenPayload {
        return try {
            val parts = idToken.split(".")
            require(parts.size == JWT_PARTS_COUNT) {
                "Invalid JWT format: expected $JWT_PARTS_COUNT parts but got ${parts.size}"
            }

            val decodedPayload = decodeBase64UrlSafe(parts[JWT_PAYLOAD_INDEX])
            val payloadJson = String(decodedPayload, Charsets.UTF_8)

            objectMapper.readValue(payloadJson, AppleIdTokenPayload::class.java)

        } catch (e: IllegalArgumentException) {
            throw AuthException(
                AuthErrorCode.INVALID_ID_TOKEN_FORMAT,
                "Invalid token format: ${e.message}"
            )
        } catch (e: JsonProcessingException) {
            throw AuthException(
                AuthErrorCode.FAILED_TO_PARSE_ID_TOKEN,
                "Failed to parse JSON: ${e.message}"
            )
        } catch (e: Exception) {
            throw AuthException(
                AuthErrorCode.FAILED_TO_PARSE_ID_TOKEN,
                "Failed to parse token: ${e.message}"
            )
        }
    }

    private fun decodeBase64UrlSafe(encoded: String): ByteArray {
        return try {
            Base64.getUrlDecoder().decode(encoded)
        } catch (e: IllegalArgumentException) {
            throw AuthException(
                AuthErrorCode.INVALID_ID_TOKEN_FORMAT,
                "Invalid Base64 encoding: ${e.message}"
            )
        }
    }

    private fun createUserInfo(payload: AppleIdTokenPayload): UserCreateInfo {
        return UserCreateInfo.of(
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
    ) {
        init {
            require(sub.isNotBlank()) { "Subject cannot be blank" }
        }
    }
}
