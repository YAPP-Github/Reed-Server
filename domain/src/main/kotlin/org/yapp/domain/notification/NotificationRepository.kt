package org.yapp.domain.notification

import org.yapp.domain.user.User

interface NotificationRepository {
    fun save(notification: Notification): Notification
    fun findByUser(user: User): Notification?
    fun findByUserId(userId: java.util.UUID): List<Notification>
    fun findAll(): List<Notification>
    fun findBySent(isSent: Boolean): List<Notification>
}
