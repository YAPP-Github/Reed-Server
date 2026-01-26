package org.yapp.apis.auth.strategy.signin

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.yapp.apis.auth.dto.response.UserCreateInfoResponse
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.auth.helper.google.GoogleIdTokenProcessor
import org.yapp.apis.auth.util.NicknameGenerator
import org.yapp.domain.user.ProviderType

@Component
class GoogleSignInStrategy(
    private val googleIdTokenProcessor: GoogleIdTokenProcessor,
) : SignInStrategy {

    private val log = KotlinLogging.logger {}

    override fun getProviderType(): ProviderType = ProviderType.GOOGLE

    override fun authenticate(credentials: SignInCredentials): UserCreateInfoResponse {
        return try {
            val googleCredentials = validateCredentials(credentials)
            val googleUserPayload = googleIdTokenProcessor.parseAndValidate(googleCredentials.idToken)
            createUserInfo(googleUserPayload)
        } catch (exception: Exception) {
            log.error("Google authentication failed", exception)
            when (exception) {
                is AuthException -> throw exception
                else -> throw AuthException(AuthErrorCode.FAILED_TO_GET_USER_INFO, exception.message)
            }
        }
    }

    private fun validateCredentials(credentials: SignInCredentials): GoogleAuthCredentials {
        return credentials as? GoogleAuthCredentials
            ?: throw AuthException(
                AuthErrorCode.INVALID_CREDENTIALS,
                "Credentials must be GoogleAuthCredentials"
            )
    }

    private fun createUserInfo(googleUser: GoogleIdToken.Payload): UserCreateInfoResponse {
        return UserCreateInfoResponse.of(
            email = googleUser.email ?: ("google_${googleUser.subject}@google.com"),
            nickname = NicknameGenerator.generate(),
            profileImageUrl = googleUser["picture"] as? String,
            providerType = ProviderType.GOOGLE,
            providerId = googleUser.subject
        )
    }
}
