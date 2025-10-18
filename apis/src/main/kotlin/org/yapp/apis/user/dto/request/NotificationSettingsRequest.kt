package org.yapp.apis.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

data class NotificationSettingsRequest(
    @field:NotNull(message = "알림 설정 여부는 필수입니다.")
    @Schema(description = "알림 설정 여부", example = "true", required = true)
    val notificationEnabled: Boolean
)
