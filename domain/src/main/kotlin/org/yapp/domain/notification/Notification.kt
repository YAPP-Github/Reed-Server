package org.yapp.domain.notification

import org.yapp.domain.user.User
import org.yapp.globalutils.util.UuidGenerator
import java.time.LocalDateTime
import java.util.UUID

data class Notification private constructor(
    val id: Id,
    val user: User,
    val title: String,
    val message: String,
    val notificationType: NotificationType,
    val isRead: Boolean = false,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
    @JvmInline
    value class Id(val value: UUID) {
        companion object {
            fun newInstance(value: UUID) = Id(value)
        }
    }

    companion object {
        fun create(
            user: User,
            title: String,
            message: String,
            notificationType: NotificationType
        ): Notification {
            return Notification(
                id = Id.newInstance(UuidGenerator.create()),
                user = user,
                title = title,
                message = message,
                notificationType = notificationType
            )
        }

        fun reconstruct(
            id: Id,
            user: User,
            title: String,
            message: String,
            notificationType: NotificationType,
            isRead: Boolean,
            createdAt: LocalDateTime?,
            updatedAt: LocalDateTime?
        ): Notification {
            return Notification(
                id = id,
                user = user,
                title = title,
                message = message,
                notificationType = notificationType,
                isRead = isRead,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}

enum class NotificationType {
    UNRECORDED,
    DORMANT
}
