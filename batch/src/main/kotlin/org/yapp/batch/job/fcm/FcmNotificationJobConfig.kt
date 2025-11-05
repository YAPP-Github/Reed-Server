package org.yapp.batch.job.fcm

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.annotation.Transactional
import org.yapp.batch.service.FcmService
import org.yapp.domain.device.Device
import org.yapp.domain.device.DeviceRepository
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
    private val deviceRepository: DeviceRepository,
    private val fcmService: FcmService
) {
    private val logger = LoggerFactory.getLogger(FcmNotificationJobConfig::class.java)

    @Scheduled(fixedRate = 60000)
    @Transactional
    fun checkAndSendNotifications() {
        logger.info("Starting notification check job")

        val sevenDaysAgo = LocalDateTime.now().minusDays(7)
        val unrecordedUsers = userRepository.findByLastActivityBeforeAndNotificationEnabled(sevenDaysAgo, true)

        var unrecordedSuccessCount = 0
        unrecordedUsers.forEach { user ->
            val devices = deviceRepository.findByUserId(user.id.value)
            devices.forEach { device ->
                val userNotifications = notificationRepository.findByUserId(user.id.value)

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
                    val (success, _) = sendNotificationToUser(
                        user = user,
                        device = device,
                        title = "\uD83D\uDCDA 잠시 멈춘 기록.. 다시 이어가 볼까요?",
                        message = "이번주에 읽은 책, 잊기 전에 기록해 보세요!",
                        notificationType = NotificationType.UNRECORDED
                    )
                    if (success) unrecordedSuccessCount++
                }
            }
        }
        val thirtyDaysAgo = LocalDateTime.now().minusDays(30)
        val dormantUsers = userRepository.findByLastActivityBeforeAndNotificationEnabled(thirtyDaysAgo, true)

        var dormantSuccessCount = 0
        dormantUsers.forEach { user ->
            val devices = deviceRepository.findByUserId(user.id.value)
            devices.forEach { device ->
                val userNotifications = notificationRepository.findByUserId(user.id.value)

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
                    val (success, _) = sendNotificationToUser(
                        user = user,
                        device = device,
                        title = "\uD83D\uDCDA Reed와 함께 독서 기록 시작",
                        message = "그동안 읽은 책을 모아 기록해 보세요!",
                        notificationType = NotificationType.DORMANT
                    )
                    if (success) dormantSuccessCount++
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
        device: Device,
        title: String,
        message: String,
        notificationType: NotificationType
    ): Pair<Boolean, Notification?> {
        try {
            val fcmToken = device.fcmToken

            if (fcmToken.isBlank()) {
                logger.info("No valid FCM token found for user: ${user.id.value}, device: ${device.id.value}")
                return Pair(false, null)
            }

            val results = fcmService.sendMulticastNotification(listOf(fcmToken), title, message)
            logger.info("Sent notifications to user ${user.id.value}: ${results.size} successful out of 1")

            if (results.isNotEmpty()) {
                val notification = Notification.create(
                    user = user,
                    title = title,
                    message = message,
                    notificationType = notificationType,
                    isSent = true,
                    sentAt = LocalDateTime.now()
                )

                val savedNotification = notificationRepository.save(notification)
                logger.info("Created new notification for user ${user.id.value} with sent status")
                return Pair(true, savedNotification)
            }

            return Pair(false, null)
        } catch (e: Exception) {
            logger.error("Error sending notification to user ${user.id.value}", e)
            return Pair(false, null)
        }
    }
}
