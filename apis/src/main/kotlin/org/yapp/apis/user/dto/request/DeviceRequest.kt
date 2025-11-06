package org.yapp.apis.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(
    name = "DeviceRequest",
    description = "DTO for device update requests"
)
data class DeviceRequest private constructor(
    @field:Schema(
        description = "디바이스 아이디",
        example = "c8a9d7d0-4f6a-4b1a-8f0a-9d8e7f6a4b1a",
        required = true
    )
    @field:NotBlank(message = "디바이스 아이디는 필수입니다.")
    val deviceId: String? = null,

    @field:Schema(
        description = "FCM 토큰",
        example = "epGzIKlHScicTBrbt26pFG:APA91bG-ZPD-KMJyS-JOiflEPUIVvrp8l9DFBN2dlNG8IHw8mFlkAPok7dVPFJR4phc9061KPztkAIjBJaryZLnv6vIJXNGQsykzDcok3wFC9LrsC-F_aKY",
        required = true
    )
    @field:NotBlank(message = "FCM 토큰은 필수입니다.")
    val fcmToken: String? = null
) {
    fun validDeviceId(): String = deviceId!!.trim()
    fun validFcmToken(): String = fcmToken!!.trim()
}