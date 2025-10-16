package org.yapp.batch.job.fcm

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.yapp.batch.service.FcmService
import org.yapp.domain.notification.Notification
import org.yapp.domain.notification.NotificationRepository
import org.yapp.domain.notification.NotificationType
import org.yapp.domain.user.User
import org.yapp.domain.user.UserRepository
import java.time.LocalDateTime

@Configuration
@EnableScheduling
class FcmNotificationJobConfig(
    private val userRepository: UserRepository,
    private val notificationRepository: NotificationRepository,
    private val fcmService: FcmService
) {
    private val logger = LoggerFactory.getLogger(FcmNotificationJobConfig::class.java)

    /**
     * Send notifications to users who haven't been active for a while
     * Runs every minute
     */
    @Scheduled(fixedRate = 60000) // 60000 ms = 1 minute
    fun checkAndSendNotifications() {
        logger.info("Starting notification check job")

        // Check for users who haven't been active for 7 days (unrecorded)
        val sevenDaysAgo = LocalDateTime.now().minusDays(7)
        val unrecordedUsers = userRepository.findByLastActivityBeforeAndNotificationEnabled(sevenDaysAgo, true)

        var unrecordedSuccessCount = 0
        unrecordedUsers.forEach { user ->
            // Check if user has already received a notification
            val existingNotifications = notificationRepository.findByUserId(user.id.value)
            val hasUnrecordedNotification = existingNotifications.any { 
                it.notificationType == NotificationType.UNRECORDED && it.isSent 
            }

            // If user hasn't received a notification yet, send one
            if (!hasUnrecordedNotification) {
                val success = sendNotificationToUser(
                    user = user,
                    title = "미기록 알림",
                    message = "이번주에 읽은 책, 잊기 전에 기록해 보세요!",
                    notificationType = NotificationType.UNRECORDED
                )
                if (success) unrecordedSuccessCount++
            }
        }

        // Check for users who haven't been active for 30 days (dormant)
        val thirtyDaysAgo = LocalDateTime.now().minusDays(30)
        val dormantUsers = userRepository.findByLastActivityBeforeAndNotificationEnabled(thirtyDaysAgo, true)

        var dormantSuccessCount = 0
        dormantUsers.forEach { user ->
            // Check if user has already received a notification
            val existingNotifications = notificationRepository.findByUserId(user.id.value)
            val hasDormantNotification = existingNotifications.any { 
                it.notificationType == NotificationType.DORMANT && it.isSent 
            }

            // If user hasn't received a notification yet, send one
            if (!hasDormantNotification) {
                val success = sendNotificationToUser(
                    user = user,
                    title = "휴면 알림",
                    message = "그동안 읽은 책을 모아 기록해 보세요!",
                    notificationType = NotificationType.DORMANT
                )
                if (success) dormantSuccessCount++
            }
        }

        // Check for users who have become active again and reset their notification status
        resetNotificationsForActiveUsers()

        logger.info("Completed notification check job. Successfully sent unrecorded notifications to $unrecordedSuccessCount out of ${unrecordedUsers.size} users and dormant notifications to $dormantSuccessCount out of ${dormantUsers.size} users")
    }

    /**
     * Reset notification status for users who have become active again
     */
    private fun resetNotificationsForActiveUsers() {
        // Get all notifications that have been sent
        val sentNotifications = notificationRepository.findBySent(true)

        sentNotifications.forEach { notification ->
            val user = notification.user
            val sentAt = notification.sentAt
            val lastActivity = user.lastActivity

            // If the user has been active since the notification was sent, reset the notification status
            if (sentAt != null && lastActivity != null && lastActivity.isAfter(sentAt)) {
                // Create a new notification with isSent = false
                val updatedNotification = Notification.reconstruct(
                    id = notification.id,
                    user = user,
                    fcmToken = notification.fcmToken,
                    title = notification.title,
                    message = notification.message,
                    notificationType = notification.notificationType,
                    isRead = notification.isRead,
                    isSent = false, // Reset to false
                    sentAt = null, // Reset to null
                    createdAt = notification.createdAt,
                    updatedAt = notification.updatedAt
                )

                // Save the updated notification
                notificationRepository.save(updatedNotification)
                logger.info("Reset notification status for user ${user.id.value} as they have been active since the notification was sent")
            }
        }
    }

    /**
     * Send a notification to a user and save it to the database
     * 
     * @param user The user to send the notification to
     * @param title The notification title
     * @param message The notification message
     * @param notificationType The type of notification
     * @return true if the notification was sent successfully, false otherwise
     */
    private fun sendNotificationToUser(
        user: User, 
        title: String, 
        message: String, 
        notificationType: NotificationType
    ): Boolean {
        try {
            // Find FCM tokens for the user
            val existingNotifications = notificationRepository.findByUserId(user.id.value)

            // Create a new notification
            val notification = Notification.create(
                user = user,
                title = title,
                message = message,
                notificationType = notificationType,
                isSent = false, // Initially not sent
                sentAt = null
            )

            // Save the notification to the database
            val savedNotification = notificationRepository.save(notification)

            // If no FCM tokens found, just save the notification without sending
            if (existingNotifications.isEmpty()) {
                logger.info("No FCM token found for user: ${user.id.value}, notification saved to database only")
                return false
            }

            // Send notification to all user's devices
            val fcmTokens = existingNotifications.map { it.fcmToken }.filter { it.isNotBlank() }
            if (fcmTokens.isEmpty()) {
                logger.info("No valid FCM tokens found for user: ${user.id.value}, notification saved to database only")
                return false
            }

            val results = fcmService.sendMulticastNotification(fcmTokens, title, message)
            logger.info("Sent notifications to user ${user.id.value}: ${results.size} successful out of ${fcmTokens.size}")

            // If notification was sent successfully, update the notification status
            if (results.isNotEmpty()) {
                // Create a new notification with isSent = true and sentAt = now
                val updatedNotification = Notification.reconstruct(
                    id = savedNotification.id,
                    user = user,
                    fcmToken = savedNotification.fcmToken,
                    title = savedNotification.title,
                    message = savedNotification.message,
                    notificationType = savedNotification.notificationType,
                    isRead = savedNotification.isRead,
                    isSent = true, // Set to true
                    sentAt = LocalDateTime.now(), // Set to now
                    createdAt = savedNotification.createdAt,
                    updatedAt = savedNotification.updatedAt
                )

                // Save the updated notification
                notificationRepository.save(updatedNotification)
                logger.info("Updated notification status for user ${user.id.value} to sent")
            }

            return results.isNotEmpty()
        } catch (e: Exception) {
            logger.error("Error sending notification to user ${user.id.value}", e)
            return false
        }
    }
}
