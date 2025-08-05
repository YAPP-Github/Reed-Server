package org.yapp.apis.user.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.yapp.domain.user.ProviderType
import org.yapp.domain.user.vo.UserProfileVO
import java.util.UUID

@Schema(
    name = "UserProfileResponse",
    description = "Response containing user profile information"
)
data class UserProfileResponse(

    @Schema(
        description = "Unique identifier of the user",
        example = "c4d46ff7-9f1b-4c5f-8262-9fa2f982a7f4"
    )
    val id: UUID,

    @Schema(
        description = "User email address",
        example = "user@example.com"
    )
    val email: String,

    @Schema(
        description = "User nickname",
        example = "HappyPanda"
    )
    val nickname: String,

    @Schema(
        description = "Social login provider type",
        example = "KAKAO"
    )
    val provider: ProviderType,

    @Schema(
        description = "Whether the user has agreed to the terms of service",
        example = "false"
    )
    val termsAgreed: Boolean
) {
    companion object {
        fun from(userProfileVO: UserProfileVO): UserProfileResponse {
            return UserProfileResponse(
                id = userProfileVO.id.value,
                email = userProfileVO.email.value,
                nickname = userProfileVO.nickname,
                provider = userProfileVO.provider,
                termsAgreed = userProfileVO.termsAgreed
            )
        }
    }
}
