package org.yapp.apis.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.yapp.apis.auth.dto.response.UserCreateInfoResponse
import org.yapp.apis.auth.util.NicknameGenerator
import org.yapp.domain.user.ProviderType

@Schema(
    name = "FindOrCreateUserRequest",
    description = "Request DTO for finding an existing user or creating a new one during social login"
)
data class FindOrCreateUserRequest private constructor(
    @field:Schema(
        description = "사용자 이메일",
        example = "user@example.com", nullable = true)
    val email: String? = null,

    @field:Schema(
        description = "사용자 닉네임",
        example = "코딩하는곰",
        nullable = true
    )
    val nickname: String? = null,

    @field:Schema(
        description = "사용자 프로필 이미지 URL",
        example = "https://example.com/image.jpg",
        nullable = true
    )
    val profileImageUrl: String? = null,

    @field:Schema(
        description = "소셜 로그인 제공자",
        example = "KAKAO"
    )
    @field:NotNull(message = "providerType은 필수입니다.")
    val providerType: ProviderType? = null,

    @field:Schema(
        description = "소셜 제공자에서 발급한 식별자",
        example = "12345678901234567890"
    )
    @field:NotBlank(message = "providerId는 필수입니다.")
    val providerId: String? = null
) {
    fun getOrDefaultEmail(): String {
        return email?.takeIf { it.isNotBlank() } ?: "${validProviderId()}@${validProviderType().name.lowercase()}.local"
    }

    fun getOrDefaultNickname(): String {
        return nickname?.takeIf { it.isNotBlank() } ?: NicknameGenerator.generate()
    }

    fun validProviderType(): ProviderType = providerType!!
    fun validProviderId(): String = providerId!!

    companion object {
        fun from(
            userCreateInfoResponse: UserCreateInfoResponse
        ): FindOrCreateUserRequest {
            return FindOrCreateUserRequest(
                email = userCreateInfoResponse.email,
                nickname = userCreateInfoResponse.nickname,
                profileImageUrl = userCreateInfoResponse.profileImageUrl,
                providerType = userCreateInfoResponse.providerType,
                providerId = userCreateInfoResponse.providerId
            )
        }
    }
}
