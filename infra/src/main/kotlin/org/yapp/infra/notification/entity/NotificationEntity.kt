package org.yapp.infra.notification.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.yapp.domain.notification.Notification
import org.yapp.domain.notification.NotificationType
import org.yapp.infra.common.BaseTimeEntity
import org.yapp.infra.user.entity.UserEntity
import java.sql.Types
import java.util.UUID

@Entity
@Table(name = "notifications")
class NotificationEntity private constructor(
    @Id
    @JdbcTypeCode(Types.VARCHAR)
    @Column(length = 36, updatable = false, nullable = false)
    val id: UUID,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val message: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false, length = 20)
    val notificationType: NotificationType,

    @Column(name = "is_read", nullable = false)
    var isRead: Boolean = false
) : BaseTimeEntity() {

    fun toDomain(user: User): Notification = Notification.reconstruct(
        id = Notification.Id.newInstance(this.id),
        user = user,
        title = this.title,
        message = this.message,
        notificationType = this.notificationType,
        isRead = this.isRead,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )

    companion object {
        fun fromDomain(notification: Notification): NotificationEntity {
            return NotificationEntity(
                id = notification.id.value,
                userId = notification.user.id.value,
                title = notification.title,
                message = notification.message,
                notificationType = notification.notificationType,
                isRead = notification.isRead
            )
        }
    }
}
