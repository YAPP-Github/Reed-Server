package org.yapp.infra.notification.repository

import org.yapp.infra.notification.entity.NotificationEntity
import org.yapp.infra.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface JpaNotificationRepository : JpaRepository<NotificationEntity, UUID> {
    fun findByUser(user: UserEntity): NotificationEntity?
    fun findByUserId(userId: UUID): List<NotificationEntity>
    fun findByIsSent(isSent: Boolean): List<NotificationEntity>
}
