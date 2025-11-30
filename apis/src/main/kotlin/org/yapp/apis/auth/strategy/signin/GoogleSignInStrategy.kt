package org.yapp.apis.auth.strategy.signin

import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.yapp.apis.auth.dto.response.UserCreateInfoResponse
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.auth.manager.GoogleApiManager
import org.yapp.apis.auth.util.NicknameGenerator
import org.yapp.domain.user.ProviderType
import org.yapp.infra.external.oauth.google.response.GoogleUserInfo

@Component
class GoogleSignInStrategy(
    private val googleApiManager: GoogleApiManager
) : SignInStrategy {

    private val log = KotlinLogging.logger {}

    override fun getProviderType(): ProviderType = ProviderType.GOOGLE

    override fun authenticate(credentials: SignInCredentials): UserCreateInfoResponse {
        return try {
            val googleCredentials = validateCredentials(credentials)
            val googleUser = googleApiManager.getUserInfo(googleCredentials.accessToken)
            createUserInfo(googleUser)
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

    private fun createUserInfo(googleUser: GoogleUserInfo): UserCreateInfoResponse {
        return UserCreateInfoResponse.of(
            email = googleUser.email ?: ("google_${googleUser.id}@google.com"),
            nickname = NicknameGenerator.generate(),
            profileImageUrl = googleUser.picture,
            providerType = ProviderType.GOOGLE,
            providerId = googleUser.id
        )
    }
}
