package org.yapp.batch.job.fcm

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.annotation.Transactional
import org.yapp.batch.service.FcmService
import org.yapp.domain.notification.Notification
import org.yapp.domain.notification.NotificationRepository
import org.yapp.domain.notification.NotificationType
import org.yapp.domain.user.User
import org.yapp.domain.user.UserRepository
import java.time.LocalDateTime
import java.util.UUID

@Configuration
@EnableScheduling
class FcmNotificationJobConfig(
    private val userRepository: UserRepository,
    private val notificationRepository: NotificationRepository,
    private val fcmService: FcmService
) {
    private val logger = LoggerFactory.getLogger(FcmNotificationJobConfig::class.java)

    @Scheduled(fixedRate = 60000)
    @Transactional
    fun checkAndSendNotifications() {
        logger.info("Starting notification check job")

        val sevenDaysAgo = LocalDateTime.now().minusDays(7)
        val unrecordedUsers = userRepository.findByLastActivityBeforeAndNotificationEnabled(sevenDaysAgo, true)

        val userNotificationsCache = mutableMapOf<UUID, List<Notification>>()

        var unrecordedSuccessCount = 0
        unrecordedUsers.forEach { user ->
            val userNotifications = userNotificationsCache.getOrPut(user.id.value) {
                notificationRepository.findByUserId(user.id.value)
            }

            val hasActiveUnrecordedNotification = userNotifications.any { 
                it.notificationType == NotificationType.UNRECORDED && it.isSent 
            }
            if (!hasActiveUnrecordedNotification) {
                if (userNotifications.isNotEmpty()) {
                    val resetNotifications = userNotifications.filter { 
                        it.notificationType == NotificationType.UNRECORDED && !it.isSent 
                    }
                    if (resetNotifications.isNotEmpty()) {
                        logger.info("Sending new unrecorded notification to user ${user.id.value} after previous notification was reset. User lastActivity: ${user.lastActivity}")
                    }
                }
                val (success, updatedNotifications) = sendNotificationToUser(
                    user = user,
                    title = "미기록 알림",
                    message = "이번주에 읽은 책, 잊기 전에 기록해 보세요!",
                    notificationType = NotificationType.UNRECORDED,
                    existingNotifications = userNotifications
                )
                if (success) unrecordedSuccessCount++
                if (updatedNotifications.isNotEmpty()) {
                    userNotificationsCache[user.id.value] = updatedNotifications
                }
            }
        }
        val thirtyDaysAgo = LocalDateTime.now().minusDays(30)
        val dormantUsers = userRepository.findByLastActivityBeforeAndNotificationEnabled(thirtyDaysAgo, true)

        var dormantSuccessCount = 0
        dormantUsers.forEach { user ->
            val userNotifications = userNotificationsCache.getOrPut(user.id.value) {
                notificationRepository.findByUserId(user.id.value)
            }

            val hasActiveDormantNotification = userNotifications.any { 
                it.notificationType == NotificationType.DORMANT && it.isSent 
            }
            if (!hasActiveDormantNotification) {
                if (userNotifications.isNotEmpty()) {
                    val resetNotifications = userNotifications.filter { 
                        it.notificationType == NotificationType.DORMANT && !it.isSent 
                    }
                    if (resetNotifications.isNotEmpty()) {
                        logger.info("Sending new dormant notification to user ${user.id.value} after previous notification was reset. User lastActivity: ${user.lastActivity}")
                    }
                }
                val (success, updatedNotifications) = sendNotificationToUser(
                    user = user,
                    title = "휴면 알림",
                    message = "그동안 읽은 책을 모아 기록해 보세요!",
                    notificationType = NotificationType.DORMANT,
                    existingNotifications = userNotifications
                )
                if (success) dormantSuccessCount++
                if (updatedNotifications.isNotEmpty()) {
                    userNotificationsCache[user.id.value] = updatedNotifications
                }
            }
        }
        resetNotificationsForActiveUsers()

        logger.info("Completed notification check job. Successfully sent unrecorded notifications to $unrecordedSuccessCount out of ${unrecordedUsers.size} users and dormant notifications to $dormantSuccessCount out of ${dormantUsers.size} users")
    }

    @Transactional
    protected fun resetNotificationsForActiveUsers() {
        val sentNotifications = notificationRepository.findBySent(true)

        sentNotifications.forEach { notification ->
            val user = notification.user
            val sentAt = notification.sentAt
            val lastActivity = user.lastActivity

            if (sentAt != null && lastActivity != null && lastActivity.isAfter(sentAt)) {
                val updatedNotification = Notification.reconstruct(
                    id = notification.id,
                    user = user,
                    fcmToken = notification.fcmToken,
                    title = notification.title,
                    message = notification.message,
                    notificationType = notification.notificationType,
                    isRead = notification.isRead,
                    isSent = false,
                    sentAt = null,
                    createdAt = notification.createdAt,
                    updatedAt = notification.updatedAt
                )

                notificationRepository.save(updatedNotification)
                logger.info("Reset notification status for user ${user.id.value} as they have been active since the notification was sent (lastActivity: $lastActivity, sentAt: $sentAt)")
            }
        }
    }

    @Transactional
    protected fun sendNotificationToUser(
        user: User, 
        title: String, 
        message: String, 
        notificationType: NotificationType,
        existingNotifications: List<Notification> = emptyList()
    ): Pair<Boolean, List<Notification>> {
        try {
            val userNotifications = if (existingNotifications.isNotEmpty()) {
                existingNotifications.toMutableList()
            } else {
                notificationRepository.findByUserId(user.id.value).toMutableList()
            }

            val existingNotification = userNotifications.find { it.notificationType == notificationType }

            val fcmTokens = userNotifications.map { it.fcmToken }.filter { it.isNotBlank() }

            if (fcmTokens.isEmpty()) {
                if (existingNotification != null) {
                    logger.info("No valid FCM tokens found for user: ${user.id.value}, using existing notification")
                    return Pair(false, userNotifications)
                }

                val notification = Notification.create(
                    user = user,
                    title = title,
                    message = message,
                    notificationType = notificationType,
                    isSent = false,
                    sentAt = null
                )

                val savedNotification = notificationRepository.save(notification)
                userNotifications.add(savedNotification)
                logger.info("No FCM token found for user: ${user.id.value}, notification saved to database only")
                return Pair(false, userNotifications)
            }

            val results = fcmService.sendMulticastNotification(fcmTokens, title, message)
            logger.info("Sent notifications to user ${user.id.value}: ${results.size} successful out of ${fcmTokens.size}")

            if (results.isNotEmpty()) {
                if (existingNotification != null) {
                    val updatedNotification = Notification.reconstruct(
                        id = existingNotification.id,
                        user = user,
                        fcmToken = existingNotification.fcmToken,
                        title = title,
                        message = message,
                        notificationType = notificationType,
                        isRead = existingNotification.isRead,
                        isSent = true,
                        sentAt = LocalDateTime.now(),
                        createdAt = existingNotification.createdAt,
                        updatedAt = existingNotification.updatedAt
                    )

                    val savedNotification = notificationRepository.save(updatedNotification)
                    val index = userNotifications.indexOfFirst { it.id == existingNotification.id }
                    if (index >= 0) {
                        userNotifications[index] = savedNotification
                    }
                    logger.info("Updated existing notification for user ${user.id.value} to sent")
                } else {
                    val notification = Notification.create(
                        user = user,
                        title = title,
                        message = message,
                        notificationType = notificationType,
                        isSent = true,
                        sentAt = LocalDateTime.now()
                    )

                    val savedNotification = notificationRepository.save(notification)
                    userNotifications.add(savedNotification)
                    logger.info("Created new notification for user ${user.id.value} with sent status")
                }
            }

            return Pair(results.isNotEmpty(), userNotifications)
        } catch (e: Exception) {
            logger.error("Error sending notification to user ${user.id.value}", e)
            return Pair(false, existingNotifications)
        }
    }
}
