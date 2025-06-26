package org.yapp.apis.auth.dto

import org.yapp.domain.auth.ProviderType

data class UserCreateInfo private constructor(
    val email: String,
    val nickname: String,
    val profileImageUrl: String? = null,
    val providerType: ProviderType,
    val providerId: String
) {
    companion object {
        fun of(
            email: String,
            nickname: String,
            profileImageUrl: String? = null,
            providerType: ProviderType,
            providerId: String,
        ): UserCreateInfo {
            return UserCreateInfo(
                email = email,
                nickname = nickname,
                profileImageUrl = profileImageUrl,
                providerType = providerType,
                providerId = providerId,
            )
        }
    }
}
