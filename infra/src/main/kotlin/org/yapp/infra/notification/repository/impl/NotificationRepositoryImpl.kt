package org.yapp.infra.notification.repository.impl

import org.springframework.stereotype.Repository
import org.yapp.domain.notification.Notification
import org.yapp.domain.notification.NotificationRepository
import org.yapp.domain.user.User
import org.yapp.infra.notification.entity.NotificationEntity
import org.yapp.infra.notification.repository.JpaNotificationRepository
import org.yapp.infra.user.entity.UserEntity
import java.util.UUID

@Repository
class NotificationRepositoryImpl(
    private val jpaNotificationRepository: JpaNotificationRepository
) : NotificationRepository {
    override fun save(notification: Notification): Notification {
        val notificationEntity = jpaNotificationRepository.save(
            NotificationEntity.fromDomain(notification)
        )
        return notificationEntity.toDomain()
    }

    override fun findByUser(user: User): Notification? {
        val userEntity = UserEntity.fromDomain(user)
        return jpaNotificationRepository.findByUser(userEntity)?.toDomain()
    }

    override fun findByUserId(userId: UUID): List<Notification> {
        return jpaNotificationRepository.findByUserId(userId).map { it.toDomain() }
    }

    override fun findAll(): List<Notification> {
        return jpaNotificationRepository.findAll().map { it.toDomain() }
    }

    override fun findBySent(isSent: Boolean): List<Notification> {
        return jpaNotificationRepository.findByIsSent(isSent).map { it.toDomain() }
    }
}
