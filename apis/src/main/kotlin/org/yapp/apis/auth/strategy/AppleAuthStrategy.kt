package org.yapp.apis.auth.strategy

import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.yapp.apis.auth.dto.response.UserCreateInfoResponse
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.auth.helper.AppleJwtHelper
import org.yapp.apis.util.NicknameGenerator
import org.yapp.domain.user.ProviderType

/**
 * Implementation of AuthStrategy for Apple authentication.
 */
@Component
class AppleAuthStrategy(
    private val appleJwtHelper: AppleJwtHelper
) : AuthStrategy {

    private val log = KotlinLogging.logger {}

    override fun getProviderType(): ProviderType = ProviderType.APPLE

    override fun authenticate(credentials: AuthCredentials): UserCreateInfoResponse {
        return try {
            val appleCredentials = validateCredentials(credentials)
            val payload = appleJwtHelper.parseIdToken(appleCredentials.idToken)
            createUserInfo(payload)
        } catch (exception: Exception) {
            log.error("Apple authentication failed", exception)
            when (exception) {
                is AuthException -> throw exception
                else -> throw AuthException(AuthErrorCode.FAILED_TO_GET_USER_INFO, exception.message)
            }
        }
    }

    private fun validateCredentials(credentials: AuthCredentials): AppleAuthCredentials {
        return credentials as? AppleAuthCredentials
            ?: throw AuthException(
                AuthErrorCode.INVALID_CREDENTIALS,
                "Credentials must be AppleAuthCredentials"
            )
    }

    private fun createUserInfo(payload: AppleJwtHelper.AppleIdTokenPayload): UserCreateInfoResponse {
        return UserCreateInfoResponse.of(
            email = payload.email,
            nickname = NicknameGenerator.generate(),
            profileImageUrl = null, // Apple doesn't provide profile image
            providerType = ProviderType.APPLE,
            providerId = payload.sub
        )
    }
}

