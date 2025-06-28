package org.yapp.apis.auth.helper

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.globalutils.annotation.Helper
import java.util.*

@Helper
class AppleJwtHelper(
    private val objectMapper: ObjectMapper
) {
    private val log = KotlinLogging.logger {}

    companion object {
        private const val JWT_PARTS_COUNT = 3
        private const val JWT_PAYLOAD_INDEX = 1
    }

    fun parseIdToken(idToken: String): AppleIdTokenPayload {
        return try {
            val parts = idToken.split(".")
            require(parts.size == JWT_PARTS_COUNT) {
                "Invalid JWT format: expected $JWT_PARTS_COUNT parts but got ${parts.size}"
            }

            val decodedPayload = decodeBase64UrlSafe(parts[JWT_PAYLOAD_INDEX])
            val payloadJson = String(decodedPayload, Charsets.UTF_8)

            objectMapper.readValue(payloadJson, AppleIdTokenPayload::class.java)

        } catch (e: IllegalArgumentException) {
            log.error("Invalid Apple ID token format", e)
            throw AuthException(
                AuthErrorCode.INVALID_ID_TOKEN_FORMAT,
                "Invalid token format: ${e.message}"
            )
        } catch (e: JsonProcessingException) {
            log.error("Failed to parse Apple ID token JSON", e)
            throw AuthException(
                AuthErrorCode.FAILED_TO_PARSE_ID_TOKEN,
                "Failed to parse JSON: ${e.message}"
            )
        } catch (e: Exception) {
            log.error("Failed to parse Apple ID token", e)
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

    data class AppleIdTokenPayload(
        val sub: String,
        val email: String?,
        val name: String?
    ) {
        init {
            require(sub.isNotBlank()) { "Subject cannot be blank" }
        }
    }
}
