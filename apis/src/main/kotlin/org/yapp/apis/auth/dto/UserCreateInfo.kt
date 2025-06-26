package org.yapp.apis.auth.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.NotBlank
import org.yapp.domain.auth.ProviderType

data class UserCreateInfo(
    @field:NotBlank(message = "이메일은 필수 입력값입니다.")
    val email: String? = null,

    @field:NotBlank(message = "닉네임은 필수 입력값입니다.")
    val nickname: String? = null,

    val profileImageUrl: String? = null,

    @field:NotNull(message = "프로바이더 타입은 필수 입력값입니다.")
    val providerType: ProviderType? = null,

    @field:NotBlank(message = "프로바이더 ID는 필수 입력값입니다.")
    val providerId: String? = null
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
