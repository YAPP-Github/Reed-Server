package org.yapp.apis.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class FcmTokenRequest(
    @field:NotBlank(message = "FCM 토큰은 필수입니다.")
    @Schema(description = "FCM 토큰", example = "epGzIKlHScicTBrbt26pFG:APA91bG-ZPD-KMJyS-JOiflEPUIVvrp8l9DFBN2dlNG8IHw8mFlkAPok7dVPFJR4phc9061KPztkAIjBJaryZLnv6vIJXNGQsykzDcok3wFC9LrsC-F_aKY", required = true)
    val fcmToken: String
) {
    fun validFcmToken(): String {
        return fcmToken.trim()
    }
}
