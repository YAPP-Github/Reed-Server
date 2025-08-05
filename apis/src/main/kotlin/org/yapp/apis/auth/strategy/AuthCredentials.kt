package org.yapp.apis.auth.strategy

import org.yapp.domain.user.ProviderType

sealed class AuthCredentials {
    abstract fun getProviderType(): ProviderType
}

data class KakaoAuthCredentials(
    val accessToken: String
) : AuthCredentials() {
    override fun getProviderType(): ProviderType = ProviderType.KAKAO
}

data class AppleAuthCredentials(
    val idToken: String,
    val authorizationCode: String
) : AuthCredentials() {
    override fun getProviderType(): ProviderType = ProviderType.APPLE
}
