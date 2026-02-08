package org.yapp.apis.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.yapp.apis.user.dto.response.CreateUserResponse
import java.util.*

@Schema(
    name = "SaveGoogleRefreshTokenRequest",
    description = "Request DTO for saving Google refresh token with user ID and authorization code"
)
data class SaveGoogleRefreshTokenRequest private constructor(
    @field:Schema(
        description = "Unique identifier of the user",
        example = "a1b2c3d4-e5f6-7890-1234-56789abcdef0"
    )
    @field:NotNull(message = "userId must not be null")
    val userId: UUID? = null,

    @field:Schema(
        description = "Google refresh token, nullable if not issued yet",
        example = "1//0g_xxxxxxxxxxxxxxxxxxxxxx"
    )
    @field:NotBlank(message = "googleRefreshToken must not be blank")
    val googleRefreshToken: String? = null
) {
    fun validUserId(): UUID = userId!!
    fun validGoogleRefreshToken(): String = googleRefreshToken!!

    companion object {
        fun of(
            userResponse: CreateUserResponse,
            googleRefreshToken: String
        ): SaveGoogleRefreshTokenRequest {
            return SaveGoogleRefreshTokenRequest(
                userId = userResponse.id,
                googleRefreshToken = googleRefreshToken
            )
        }
    }
}
