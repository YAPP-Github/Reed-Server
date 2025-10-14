package org.yapp.domain.notification

interface NotificationRepository {
    fun save(notification: Notification): Notification
}
