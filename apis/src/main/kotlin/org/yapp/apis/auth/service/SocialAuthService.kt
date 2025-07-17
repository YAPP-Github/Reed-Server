package org.yapp.apis.auth.service

import org.springframework.stereotype.Service
import org.yapp.apis.auth.strategy.AuthCredentials
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.auth.strategy.AuthStrategy

@Service
class SocialAuthService(
    private val strategies: List<AuthStrategy>
) {

    fun resolve(credentials: AuthCredentials): AuthStrategy {
        return strategies.find { it.getProviderType() == credentials.getProviderType() }
            ?: throw AuthException(
                AuthErrorCode.UNSUPPORTED_PROVIDER_TYPE,
                "Unsupported provider type: ${credentials.getProviderType()}"
            )
    }
}
