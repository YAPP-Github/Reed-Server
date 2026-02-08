package org.yapp.apis.auth.strategy.signin

import org.yapp.domain.user.ProviderType

sealed class SignInCredentials {
    abstract fun getProviderType(): ProviderType
}

data class KakaoAuthCredentials(
    val accessToken: String,
) : SignInCredentials() {
    override fun getProviderType(): ProviderType = ProviderType.KAKAO
}

data class AppleAuthCredentials(
    val idToken: String,
    val authorizationCode: String,
) : SignInCredentials() {
    override fun getProviderType(): ProviderType = ProviderType.APPLE
}

data class GoogleAuthCredentials(
    val idToken: String,
    val authorizationCode: String,
) : SignInCredentials() {
    override fun getProviderType(): ProviderType {
        return ProviderType.GOOGLE
    }
}
