package org.yapp.domain.notification

import org.yapp.domain.user.User
import org.yapp.globalutils.util.UuidGenerator
import java.time.LocalDateTime
import java.util.UUID

data class Notification private constructor(
    val id: Id,
    val userId: User.Id,
    val title: String,
    val message: String,
    val notificationType: NotificationType,
    val isRead: Boolean = false,
    val isSent: Boolean = false,
    val sentAt: LocalDateTime? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
    fun reset(): Notification {
        return this.copy(
            isSent = false,
            sentAt = null
        )
    }

    companion object {
        fun create(
            userId: UUID,
            title: String,
            message: String,
            notificationType: NotificationType,
            isSent: Boolean = false,
            sentAt: LocalDateTime? = null
        ): Notification {
            return Notification(
                id = Id.newInstance(UuidGenerator.create()),
                userId = User.Id.newInstance(userId),
                title = title,
                message = message,
                notificationType = notificationType,
                isSent = isSent,
                sentAt = sentAt
            )
        }

        fun reconstruct(
            id: Id,
            userId: User.Id,
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
                userId = userId,
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

    @JvmInline
    value class Id(val value: UUID) {
        companion object {
            fun newInstance(value: UUID) = Id(value)
        }
    }
}
