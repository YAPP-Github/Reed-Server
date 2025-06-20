package org.yapp.apis.auth.service

import org.yapp.domain.auth.ProviderType


interface AuthCredentials {
    fun getProviderType(): ProviderType
}

data class KakaoAuthCredentials(
    val accessToken: String
) : AuthCredentials {
    override fun getProviderType(): ProviderType = ProviderType.KAKAO
}

data class AppleAuthCredentials(
    val idToken: String
) : AuthCredentials {
    override fun getProviderType(): ProviderType = ProviderType.APPLE
}
