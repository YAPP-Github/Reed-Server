package org.yapp.infra.notification.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.yapp.infra.notification.entity.NotificationEntity
import java.util.UUID

interface JpaNotificationRepository : JpaRepository<NotificationEntity, UUID> {
}
