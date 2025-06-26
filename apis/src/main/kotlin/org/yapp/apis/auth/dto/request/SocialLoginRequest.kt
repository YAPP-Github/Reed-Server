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
data class SocialLoginRequest private constructor(
    @Schema(description = "Type of social login provider", example = "KAKAO", required = true)
    @field:NotBlank(message = "Provider type is required")
    val providerType: String? = null,

    @Schema(description = "OAuth token issued by the social provider", example = "eyJ...", required = true)
    @field:NotBlank(message = "OAuth token is required")
    val oauthToken: String? = null
) {
    fun validProviderType(): String = providerType!!
    fun validOauthToken(): String = oauthToken!!

    companion object {
        fun toCredentials(request: SocialLoginRequest): AuthCredentials {
            val provider = try {
                ProviderType.valueOf(request.validProviderType().uppercase())
            } catch (e: IllegalArgumentException) {
                throw AuthException(
                    AuthErrorCode.UNSUPPORTED_PROVIDER_TYPE,
                    "Unsupported provider type: ${request.validProviderType()}"
                )
            }

            return when (provider) {
                ProviderType.KAKAO -> KakaoAuthCredentials(request.validOauthToken())
                ProviderType.APPLE -> AppleAuthCredentials(request.validOauthToken())
            }
        }
    }
}
