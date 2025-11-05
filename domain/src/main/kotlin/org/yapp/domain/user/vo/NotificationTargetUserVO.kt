package org.yapp.domain.user.vo

import org.yapp.domain.user.User
import java.time.LocalDateTime
import java.util.UUID

data class NotificationTargetUserVO private constructor(
    val id: UUID,
    val email: String,
    val nickname: String,
    val lastActivity: LocalDateTime?
) {
    companion object {
        fun from(user: User): NotificationTargetUserVO {
            return NotificationTargetUserVO(
                id = user.id.value,
                email = user.email.value,
                nickname = user.nickname,
                lastActivity = user.lastActivity
            )
        }
    }
}
