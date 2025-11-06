package org.yapp.domain.notification

import org.yapp.domain.user.User
import org.yapp.globalutils.annotation.DomainService
import java.time.LocalDateTime

@DomainService
class NotificationDomainService(
    private val notificationRepository: NotificationRepository
) {
    fun hasActiveNotification(userId: User.Id, notificationType: NotificationType): Boolean {
        val userNotifications = notificationRepository.findByUserId(userId.value)
        return userNotifications.any {
            it.notificationType == notificationType && it.isSent
        }
    }

    fun createAndSaveNotification(
        userId: User.Id,
        title: String,
        message: String,
        notificationType: NotificationType
    ) {
        val notification = Notification.create(
            userId = userId.value,
            title = title,
            message = message,
            notificationType = notificationType,
            isSent = true,
            sentAt = LocalDateTime.now()
        )
        notificationRepository.save(notification)
    }

    fun findSentNotifications(): List<Notification> {
        return notificationRepository.findBySent(true)
    }

    fun save(notification: Notification) {
        notificationRepository.save(notification)
    }
}
