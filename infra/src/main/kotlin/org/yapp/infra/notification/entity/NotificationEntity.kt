package org.yapp.infra.notification.entity

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.yapp.domain.notification.Notification
import org.yapp.domain.notification.NotificationType
import org.yapp.infra.common.BaseTimeEntity
import org.yapp.infra.user.entity.UserEntity
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,

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
                user = UserEntity.fromDomain(notification.user),
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
            user = this.user.toDomain(),
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
