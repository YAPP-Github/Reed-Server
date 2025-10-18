package org.yapp.apis.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

@Schema(
    name = "NotificationSettingsRequest",
    description = "DTO for notification settings update requests"
)
data class NotificationSettingsRequest private constructor(
    @field:Schema(
        description = "알림 설정 여부",
        example = "true",
        required = true
    )
    @field:NotNull(message = "알림 설정 여부는 필수입니다.")
    val notificationEnabled: Boolean? = null
) {
    fun validNotificationEnabled(): Boolean = notificationEnabled!!
}