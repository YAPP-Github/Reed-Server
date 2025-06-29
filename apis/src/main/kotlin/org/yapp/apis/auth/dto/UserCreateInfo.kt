package org.yapp.apis.auth.dto

import org.yapp.domain.auth.ProviderType

data class UserCreateInfo private constructor(
    val email: String?,
    val nickname: String?,
    val profileImageUrl: String? = null,
    val providerType: ProviderType,
    val providerId: String
) {
    companion object {
        fun of(
            email: String?,
            nickname: String?,
            profileImageUrl: String? = null,
            providerType: ProviderType?,
            providerId: String?
        ): UserCreateInfo {
            val validEmail = email?.takeIf { it.isNotBlank() }?.trim()
            val validNickname = nickname?.takeIf { it.isNotBlank() }?.trim()

            requireNotNull(providerType) { "ProviderType은 필수입니다." }
            require(!providerId.isNullOrBlank()) { "providerId는 필수입니다." }

            return UserCreateInfo(
                email = validEmail,
                nickname = validNickname,
                profileImageUrl = profileImageUrl,
                providerType = providerType,
                providerId = providerId.trim()
            )
        }
    }
}
