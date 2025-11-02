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
    val isSent: Boolean = false,
    val sentAt: LocalDateTime? = null,
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
            notificationType: NotificationType,
            isSent: Boolean = false,
            sentAt: LocalDateTime? = null
        ): Notification {
            return Notification(
                id = Id.newInstance(UuidGenerator.create()),
                user = user,
                title = title,
                message = message,
                notificationType = notificationType,
                isSent = isSent,
                sentAt = sentAt
            )
        }

        fun reconstruct(
            id: Id,
            user: User,
            title: String,
            message: String,
            notificationType: NotificationType,
            isRead: Boolean,
            isSent: Boolean = false,
            sentAt: LocalDateTime? = null,
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
                isSent = isSent,
                sentAt = sentAt,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}
