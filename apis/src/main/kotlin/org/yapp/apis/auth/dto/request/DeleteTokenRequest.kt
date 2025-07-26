package org.yapp.apis.auth.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import org.yapp.apis.auth.dto.response.RefreshTokenResponse

@Schema(
    name = "DeleteTokenRequest",
    description = "Request DTO for deleting a refresh token"
)
data class DeleteTokenRequest private constructor(
    @field:NotBlank(message = "Refresh token must not be blank.")
    @Schema(description = "Refresh token to be deleted", example = "eyJhbGciOiJIUz...")
    val refreshToken: String? = null
) {
    fun validRefreshToken() = refreshToken!!

    companion object {
        fun from(refreshTokenResponse: RefreshTokenResponse): DeleteTokenRequest {
            return DeleteTokenRequest(refreshToken = refreshTokenResponse.refreshToken)
        }
    }
}
