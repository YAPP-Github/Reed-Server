package org.yapp.apis.auth.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.yapp.domain.user.ProviderType

@Schema(
    name = "UserCreateInfoResponse",
    description = "Response DTO containing user information for newly registered users via social login"
)
data class UserCreateInfoResponse private constructor(
    @Schema(
        description = "사용자 이메일",
        example = "user@example.com",
        nullable = true
    )
    val email: String?,

    @Schema(
        description = "사용자 닉네임",
        example = "코딩하는곰", nullable = true
    )
    val nickname: String?,

    @Schema(
        description = "사용자 프로필 이미지 URL",
        example = "https://example.com/image.jpg", nullable = true
    )
    val profileImageUrl: String? = null,

    @Schema(
        description = "소셜 로그인 제공자",
        example = "KAKAO")

    val providerType: ProviderType,

    @Schema(
        description = "소셜 제공자에서 발급한 식별자",
        example = "12345678901234567890"
    )
    val providerId: String
) {
    companion object {
        fun of(
            email: String?,
            nickname: String?,
            profileImageUrl: String? = null,
            providerType: ProviderType,
            providerId: String
        ): UserCreateInfoResponse {
            return UserCreateInfoResponse(
                email = email,
                nickname = nickname,
                profileImageUrl = profileImageUrl,
                providerType = providerType,
                providerId = providerId.trim()
            )
        }
    }
}
