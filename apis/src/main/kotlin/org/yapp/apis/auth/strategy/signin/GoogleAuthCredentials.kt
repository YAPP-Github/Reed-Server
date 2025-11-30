package org.yapp.apis.auth.strategy.signin

import org.yapp.domain.user.ProviderType

data class GoogleAuthCredentials(
    val accessToken: String,
) : SignInCredentials() {
    override fun getProviderType(): ProviderType {
        return ProviderType.GOOGLE
    }
}
