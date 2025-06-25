package org.yapp.domain.user.vo

import org.yapp.domain.auth.ProviderType

data class SocialUserProfile private constructor(
    val email: String,
    val nickname: String,
    val profileImageUrl: String?,
    val providerType: ProviderType,
    val providerId: String
) {
    init {
        require(email.isNotBlank()) { "Email must not be blank" }
        require(email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))) { "Email format is invalid" }
        require(nickname.isNotBlank()) { "Nickname must not be blank" }
        require(nickname.length in 2..30) { "Nickname length must be between 2 and 30" }
        require(providerId.isNotBlank()) { "ProviderId must not be blank" }
        profileImageUrl?.let {
            require(it.startsWith("http")) { "ProfileImageUrl must be a valid URL" }
        }
    }

    companion object {
        fun newInstance(
            email: String,
            nickname: String,
            profileImageUrl: String?,
            providerType: ProviderType,
            providerId: String
        ): SocialUserProfile {
            return SocialUserProfile(email, nickname, profileImageUrl, providerType, providerId)
        }
    }
}
