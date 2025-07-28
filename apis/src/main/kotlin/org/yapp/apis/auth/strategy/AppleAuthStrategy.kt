package org.yapp.apis.auth.strategy

import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.yapp.apis.auth.dto.response.UserCreateInfoResponse
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.auth.helper.apple.AppleIdTokenProcessor
import org.yapp.apis.auth.util.NicknameGenerator
import org.yapp.domain.user.ProviderType

@Component
class AppleAuthStrategy(
    private val appleIdTokenProcessor: AppleIdTokenProcessor
) : AuthStrategy {
    private val log = KotlinLogging.logger {}

    override fun getProviderType(): ProviderType = ProviderType.APPLE

    override fun authenticate(credentials: AuthCredentials): UserCreateInfoResponse {
        val appleCredentials = validateCredentials(credentials)

        val payload = appleIdTokenProcessor.parseAndValidate(appleCredentials.idToken)

        log.info { "Apple ID Token validated successfully. sub=${payload.sub}, email=${payload.email}" }

        return createUserInfo(payload)
    }

    private fun validateCredentials(credentials: AuthCredentials): AppleAuthCredentials {
        return credentials as? AppleAuthCredentials
            ?: throw AuthException(
                AuthErrorCode.INVALID_CREDENTIALS,
                "Credentials must be AppleAuthCredentials"
            )
    }

    private fun createUserInfo(payload: AppleIdTokenProcessor.AppleIdTokenPayload): UserCreateInfoResponse {
        return UserCreateInfoResponse.of(
            email = payload.email,
            nickname = NicknameGenerator.generate(),
            profileImageUrl = null, // Apple doesn't provide profile image
            providerType = ProviderType.APPLE,
            providerId = payload.sub
        )
    }
}

