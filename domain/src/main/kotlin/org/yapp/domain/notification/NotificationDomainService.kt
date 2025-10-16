package org.yapp.domain.notification

import org.yapp.domain.user.User
import org.yapp.globalutils.annotation.DomainService

@DomainService
class NotificationDomainService(
    private val notificationRepository: NotificationRepository
) {
    fun registerToken(user: User, token: String): Notification {
        val notification = notificationRepository.findByUser(user)?.let {
            Notification.reconstruct(
                id = it.id,
                user = user,
                fcmToken = token,
                title = it.title,
                message = it.message,
                notificationType = it.notificationType,
                isRead = it.isRead,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        } ?: Notification.create(
            user = user,
            title = "FCM Token Registration",
            message = "FCM Token has been registered",
            notificationType = NotificationType.UNRECORDED
        )
        return notificationRepository.save(notification)
    }

    fun findTokensByUserId(userId: java.util.UUID): List<String> {
        return notificationRepository.findByUserId(userId).map { it.fcmToken }
    }
}
