package org.yapp.apis.auth.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import org.yapp.apis.auth.dto.AppleAuthCredentials
import org.yapp.apis.auth.dto.AuthCredentials
import org.yapp.apis.auth.dto.KakaoAuthCredentials
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.domain.auth.ProviderType

@Schema(
    name = "SocialLoginRequest",
    description = "DTO for social login requests"
)
data class SocialLoginRequest(

    @Schema(
        description = "Type of social login provider (e.g., KAKAO, APPLE)",
        example = "KAKAO",
        required = true
    )
    @field:NotBlank(message = "Provider type is required")
    val providerType: String,

    @Schema(
        description = "OAuth token issued by the social provider",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        required = true
    )
    @field:NotBlank(message = "OAuth token is required")
    val oauthToken: String
) {
    companion object {
        fun toCredentials(request: SocialLoginRequest): AuthCredentials {
            val provider = try {
                ProviderType.valueOf(request.providerType.uppercase())
            } catch (e: IllegalArgumentException) {
                throw AuthException(
                    AuthErrorCode.UNSUPPORTED_PROVIDER_TYPE,
                    "Unsupported provider type: ${request.providerType}"
                )
            }

            return when (provider) {
                ProviderType.KAKAO -> KakaoAuthCredentials(request.oauthToken)
                ProviderType.APPLE -> AppleAuthCredentials(request.oauthToken)
            }
        }
    }
}
