package org.yapp.domain.notification

import org.yapp.domain.user.User
import java.util.UUID

interface NotificationRepository {
    fun save(notification: Notification): Notification
    fun findByUser(user: User): Notification?
    fun findByUserId(userId: UUID): List<Notification>
    fun findAll(): List<Notification>
    fun findBySent(isSent: Boolean): List<Notification>
}
