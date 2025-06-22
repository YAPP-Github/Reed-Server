package org.yapp.apis.auth.service

import org.yapp.domain.auth.ProviderType

sealed class AuthCredentials {
    abstract fun getProviderType(): ProviderType
}

data class KakaoAuthCredentials(
    val accessToken: String
) : AuthCredentials() {
    override fun getProviderType(): ProviderType = ProviderType.KAKAO
}

data class AppleAuthCredentials(
    val idToken: String
) : AuthCredentials() {
    override fun getProviderType(): ProviderType = ProviderType.APPLE
}
