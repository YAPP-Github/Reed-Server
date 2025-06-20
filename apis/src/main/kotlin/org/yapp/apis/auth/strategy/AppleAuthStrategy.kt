package org.yapp.apis.auth.strategy

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
class AppleAuthStrategy : AuthStrategy {

    override fun getProviderType(): ProviderType = ProviderType.APPLE

    override fun authenticate(credentials: AuthCredentials): User {
        if (credentials !is AppleAuthCredentials) {
            throw AuthException(AuthErrorCode.INVALID_CREDENTIALS, "Credentials must be AppleAuthCredentials")
        }

        val payload = parseIdToken(credentials.idToken)

        return User(
            email = payload.email ?: throw AuthException(AuthErrorCode.EMAIL_NOT_FOUND),
            nickname = NicknameGenerator.generate(),
            profileImageUrl = null, // Apple doesn't provide profile image
            providerType = ProviderType.APPLE,
            providerId = payload.sub
        )
    }


    private fun parseIdToken(idToken: String): AppleIdTokenPayload {
        try {
            val parts = idToken.split(".")
            if (parts.size != 3) {
                throw AuthException(AuthErrorCode.INVALID_ID_TOKEN_FORMAT)
            }

            val payload = parts[1]
            val decodedPayload = Base64.getUrlDecoder().decode(payload)
            val payloadJson = String(decodedPayload)

            // In a real implementation, you would use a JSON library to parse the payload
            // For simplicity, we'll just extract the required fields manually
            val sub = extractField(payloadJson, "\"sub\":\"", "\"")
                ?: throw AuthException(AuthErrorCode.SUBJECT_NOT_FOUND)
            val email = extractField(payloadJson, "\"email\":\"", "\"")
            val name = extractField(payloadJson, "\"name\":\"", "\"")

            return AppleIdTokenPayload(sub, email, name)
        } catch (e: Exception) {
            throw AuthException(AuthErrorCode.FAILED_TO_PARSE_ID_TOKEN, "Failed to parse ID token: ${e.message}")
        }
    }


    private fun extractField(json: String, prefix: String, suffix: String): String? {
        val startIndex = json.indexOf(prefix)
        if (startIndex == -1) {
            return null
        }

        val valueStartIndex = startIndex + prefix.length
        val valueEndIndex = json.indexOf(suffix, valueStartIndex)
        if (valueEndIndex == -1) {
            return null
        }

        return json.substring(valueStartIndex, valueEndIndex)
    }

    data class AppleIdTokenPayload(
        val sub: String,
        val email: String?,
        val name: String?
    )
}
