package org.yapp.apis.user.dto.request

import jakarta.validation.constraints.NotNull

data class NotificationSettingsRequest private constructor(
    @field:NotNull
    val notificationEnabled: Boolean? = null
) {
    fun validNotificationEnabled(): Boolean = notificationEnabled!!
}
