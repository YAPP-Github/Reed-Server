package org.yapp.apis.auth.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.yapp.apis.user.dto.response.WithdrawTargetUserResponse
import org.yapp.domain.user.ProviderType
import java.util.*

@Schema(description = "회원 탈퇴 처리 시 내부적으로 사용되는 요청 DTO")
data class WithdrawStrategyRequest private constructor(
    @field:NotNull(message = "사용자 ID는 필수 값입니다.")
    @field:Schema(
        description = "사용자 고유 ID",
        example = "123e4567-e89b-12d3-a456-426614174000"
    )
    val userId: UUID,

    @field:NotNull(message = "소셜 로그인 제공자 타입은 필수 값입니다.")
    @field:Schema(
        description = "소셜 로그인 제공자 타입",
        example = "KAKAO"
    )
    val providerType: ProviderType,

    @field:NotBlank(message = "소셜 로그인 제공자로부터 발급받은 고유 ID는 필수 값입니다.")
    @field:Schema(
        description = "소셜 로그인 제공자로부터 발급받은 고유 ID",
        example = "21412412412"
    )
    val providerId: String,

    @field:Schema(
        description = "Apple 로그인 시 발급받은 리프레시 토큰 (Apple 로그인 회원 탈퇴 시에만 필요)",
        example = "r_abc123def456ghi789jkl0mnopqrstu",
        required = false
    )
    val appleRefreshToken: String?,
    @field:Schema(
        description = "Google 로그인 시 발급받은 리프레시 토큰 (Google 로그인 회원 탈퇴 시에만 필요)",
        example = "1//0g_xxxxxxxxxxxxxxxxxxxxxx",
        required = false
    )
    val googleRefreshToken: String?
) {
    companion object {
        fun from(response: WithdrawTargetUserResponse): WithdrawStrategyRequest {
            return WithdrawStrategyRequest(
                userId = response.id,
                providerType = response.providerType,
                providerId = response.providerId,
                appleRefreshToken = response.appleRefreshToken,
                googleRefreshToken = response.googleRefreshToken
            )
        }
    }
}
