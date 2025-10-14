package org.yapp.infra.notification.repository.impl

import org.springframework.stereotype.Repository
import org.yapp.domain.notification.Notification
import org.yapp.domain.notification.NotificationRepository
import org.yapp.infra.notification.entity.NotificationEntity
import org.yapp.infra.notification.repository.JpaNotificationRepository

import org.yapp.infra.user.repository.JpaUserRepository

@Repository
class NotificationRepositoryImpl(
    private val jpaNotificationRepository: JpaNotificationRepository,
    private val jpaUserRepository: JpaUserRepository
) : NotificationRepository {
    override fun save(notification: Notification): Notification {
        jpaNotificationRepository.save(NotificationEntity.fromDomain(notification))
        return notification
    }
}
