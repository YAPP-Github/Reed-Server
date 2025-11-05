package org.yapp.infra.notification.entity

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.yapp.domain.notification.Notification
import org.yapp.domain.notification.NotificationType
import org.yapp.domain.user.User
import org.yapp.infra.common.BaseTimeEntity
import java.sql.Types
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "notification")
class NotificationEntity(
    @Id
    @JdbcTypeCode(Types.VARCHAR)
    @Column(length = 36, updatable = false, nullable = false)
    val id: UUID,

    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "user_id", length = 36, nullable = false)
    val userId: UUID,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false)
    var message: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    var notificationType: NotificationType,

    @Column(name = "is_read", nullable = false)
    var isRead: Boolean = false,

    @Column(name = "is_sent", nullable = false)
    var isSent: Boolean = false,

    @Column(name = "sent_at")
    var sentAt: LocalDateTime? = null
) : BaseTimeEntity() {

    companion object {
        fun fromDomain(notification: Notification): NotificationEntity {
            return NotificationEntity(
                id = notification.id.value,
                userId = notification.userId.value,
                title = notification.title,
                message = notification.message,
                notificationType = notification.notificationType,
                isRead = notification.isRead,
                isSent = notification.isSent,
                sentAt = notification.sentAt
            )
        }
    }

    fun toDomain(): Notification {
        return Notification.reconstruct(
            id = Notification.Id.newInstance(this.id),
            userId = User.Id.newInstance(this.userId),
            title = this.title,
            message = this.message,
            notificationType = this.notificationType,
            isRead = this.isRead,
            isSent = this.isSent,
            sentAt = this.sentAt,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }
}
