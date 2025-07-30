package org.yapp.apis.auth.strategy

import org.springframework.stereotype.Service
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException

@Service
class AuthStrategyResolver(
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
