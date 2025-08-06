package org.yapp.apis.auth.strategy.signin

import org.springframework.stereotype.Service
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException

@Service
class SignInStrategyResolver(
    private val strategies: List<SignInStrategy>
) {
    fun resolve(credentials: SignInCredentials): SignInStrategy {
        return strategies.find { it.getProviderType() == credentials.getProviderType() }
            ?: throw AuthException(
                AuthErrorCode.UNSUPPORTED_PROVIDER_TYPE,
                "Unsupported provider type: ${credentials.getProviderType()}"
            )
    }
}
