package org.yapp.apis.user.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.yapp.domain.user.ProviderType
import org.yapp.domain.user.vo.WithdrawTargetUserVO
import java.util.UUID

@Schema(
    name = "WithdrawUserResponse",
    description = "회원 탈퇴에 필요한 사용자 정보 응답 DTO"
)
data class WithdrawTargetUserResponse private constructor(

    @field:Schema(description = "사용자 ID")
    val id: UUID,

    @field:Schema(description = "소셜 로그인 제공사 타입")
    val providerType: ProviderType,

    @field:Schema(description = "소셜 제공사로부터 발급받은 고유 ID")
    val providerId: String,

    @field:Schema(description = "Apple Refresh Token (애플 회원 탈퇴 시 필요, 카카오는 null)")
    val appleRefreshToken: String? = null,
    @field:Schema(description = "Google Refresh Token (구글 회원 탈퇴 시 필요, 카카오/애플은 null)")
    val googleRefreshToken: String? = null
) {
    companion object {
        fun from(vo: WithdrawTargetUserVO): WithdrawTargetUserResponse {
            return WithdrawTargetUserResponse(
                id = vo.id.value,
                providerType = vo.providerType,
                providerId = vo.providerId.value,
                appleRefreshToken = vo.appleRefreshToken,
                googleRefreshToken = vo.googleRefreshToken
            )
        }
    }
}
