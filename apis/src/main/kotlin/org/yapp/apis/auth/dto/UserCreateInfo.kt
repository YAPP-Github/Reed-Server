package org.yapp.apis.auth.dto

import org.jetbrains.annotations.NotNull
import org.yapp.domain.auth.ProviderType

data class UserCreateInfo private constructor(
    @field:NotNull
    val email: String,

    @field:NotNull
    val nickname: String,

    val profileImageUrl: String? = null,

    @field:NotNull
    val providerType: ProviderType,

    @field:NotNull
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
