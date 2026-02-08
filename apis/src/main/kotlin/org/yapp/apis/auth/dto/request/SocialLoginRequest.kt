package org.yapp.apis.auth.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import org.yapp.apis.auth.exception.AuthErrorCode
import org.yapp.apis.auth.exception.AuthException
import org.yapp.apis.auth.strategy.signin.AppleAuthCredentials
import org.yapp.apis.auth.strategy.signin.GoogleAuthCredentials
import org.yapp.apis.auth.strategy.signin.KakaoAuthCredentials
import org.yapp.apis.auth.strategy.signin.SignInCredentials
import org.yapp.domain.user.ProviderType

@Schema(
    name = "SocialLoginRequest",
    description = "DTO for social login requests"
)
data class SocialLoginRequest private constructor(
    @field:Schema(
        description = "Type of social login provider",
        example = "KAKAO",
        required = true
    )
    @field:NotBlank(message = "Provider type is required")
    val providerType: String? = null,

    @field:Schema(
        description = "OAuth token issued by the social provider",
        example = "eyJ...",
        required = true
    )
    @field:NotBlank(message = "OAuth token is required")
    val oauthToken: String? = null,

    @field:Schema(
        description = "Authorization code used to issue access/refresh tokens (required for Apple and Google login)",
        example = "c322a426...",
        required = false
    )
    val authorizationCode: String? = null
) {
    fun validProviderType(): String = providerType!!
    fun validOauthToken(): String = oauthToken!!

    companion object {
        fun toCredentials(request: SocialLoginRequest): SignInCredentials {
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
                ProviderType.APPLE -> {
                    val authCode = request.authorizationCode
                        ?: throw AuthException(
                            AuthErrorCode.INVALID_REQUEST,
                            "Apple login requires an authorization code."
                        )
                    AppleAuthCredentials(request.validOauthToken(), authCode)
                }

                ProviderType.GOOGLE -> {
                    val authCode = request.authorizationCode
                        ?: throw AuthException(
                            AuthErrorCode.INVALID_REQUEST,
                            "Google login requires an authorization code."
                        )
                    GoogleAuthCredentials(request.validOauthToken(), authCode)
                }
            }
        }
    }
}
