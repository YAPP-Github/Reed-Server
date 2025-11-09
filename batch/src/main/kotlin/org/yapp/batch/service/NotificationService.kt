package org.yapp.batch.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.yapp.domain.device.DeviceDomainService
import org.yapp.domain.device.vo.DeviceVO
import org.yapp.domain.notification.NotificationDomainService
import org.yapp.domain.notification.NotificationType
import org.yapp.domain.user.User
import org.yapp.domain.user.UserDomainService
import org.yapp.domain.user.vo.NotificationTargetUserVO

@Service
class NotificationService(
    private val userDomainService: UserDomainService,
    private val notificationDomainService: NotificationDomainService,
    private val deviceDomainService: DeviceDomainService,
    private val fcmService: FcmService
) {
    private val logger = LoggerFactory.getLogger(NotificationService::class.java)

    companion object {
        private const val UNRECORDED_NOTIFICATION_TITLE = "📚 잠시 멈춘 기록.. 다시 이어가 볼까요?"
        private const val UNRECORDED_NOTIFICATION_MESSAGE = "이번주에 읽은 책, 잊기 전에 기록해 보세요!"
        private const val DORMANT_NOTIFICATION_TITLE = "📚 Reed와 함께 독서 기록 시작"
        private const val DORMANT_NOTIFICATION_MESSAGE = "그동안 읽은 책을 모아 기록해 보세요!"
    }

    @Transactional
    fun sendUnrecordedNotifications(daysThreshold: Int): Pair<Int, Int> {
        return sendNotificationsByType(
            daysThreshold = daysThreshold,
            notificationType = NotificationType.UNRECORDED,
            title = UNRECORDED_NOTIFICATION_TITLE,
            message = UNRECORDED_NOTIFICATION_MESSAGE,
            findUsers = { userDomainService.findUnrecordedUsers(it) }
        )
    }

    @Transactional
    fun sendDormantNotifications(daysThreshold: Int): Pair<Int, Int> {
        return sendNotificationsByType(
            daysThreshold = daysThreshold,
            notificationType = NotificationType.DORMANT,
            title = DORMANT_NOTIFICATION_TITLE,
            message = DORMANT_NOTIFICATION_MESSAGE,
            findUsers = { userDomainService.findDormantUsers(it) }
        )
    }

    private fun sendNotificationsByType(
        daysThreshold: Int,
        notificationType: NotificationType,
        title: String,
        message: String,
        findUsers: (Int) -> List<NotificationTargetUserVO>
    ): Pair<Int, Int> {
        logger.info("Starting $notificationType notifications (threshold: $daysThreshold days)")

        val users = findUsers(daysThreshold)
        logger.info("Found ${users.size} $notificationType users")

        var successUserCount = 0
        var successDeviceCount = 0

        users.forEach { user ->
            val (success, deviceCount) = sendNotificationsToUser(
                user = user,
                title = title,
                message = message,
                notificationType = notificationType
            )

            if (success) {
                successUserCount++
                successDeviceCount += deviceCount
            }
        }

        logger.info("Completed $notificationType notifications: $successUserCount users, $successDeviceCount devices")
        return Pair(successUserCount, successDeviceCount)
    }

    private fun sendNotificationsToUser(
        user: NotificationTargetUserVO,
        title: String,
        message: String,
        notificationType: NotificationType
    ): Pair<Boolean, Int> {
        val userId = User.Id.newInstance(user.id)
        if (notificationDomainService.hasActiveNotification(userId, notificationType)) {
            logger.info("User ${user.id} already has active $notificationType notification, skipping")
            return Pair(false, 0)
        }

        val devices = deviceDomainService.findDevicesByUserId(user.id)
        if (devices.isEmpty()) {
            logger.info("No devices found for user ${user.id}")
            return Pair(false, 0)
        }

        val successDeviceCount = sendToDevices(devices, title, message)
        if (successDeviceCount > 0) {
            notificationDomainService.createAndSaveNotification(
                userId = userId,
                title = title,
                message = message,
                notificationType = notificationType
            )
            return Pair(true, successDeviceCount)
        }

        logger.info("Failed to send notification to any device for user ${user.id}")
        return Pair(false, 0)
    }

    private fun sendToDevices(
        devices: List<DeviceVO>,
        title: String,
        message: String
    ): Int {
        val validTokens = devices
            .map { it.fcmToken }
            .filter { it.isNotBlank() }

        if (validTokens.isEmpty()) {
            logger.warn("No valid FCM tokens found for devices: {}", devices.map { it.id })
            return 0
        }

        val result = fcmService.sendMulticastNotification(validTokens, title, message)

        if (result.invalidTokens.isNotEmpty()) {
            logger.info("Found ${result.invalidTokens.size} invalid tokens to remove.")
            deviceDomainService.removeDevicesByTokens(result.invalidTokens)
        }

        return result.successCount
    }

    @Transactional
    fun resetNotificationsForActiveUsers() {
        val sentNotifications = notificationDomainService.findSentNotifications()

        sentNotifications.forEach { notification ->
            val sentAt = notification.sentAt
            if (sentAt != null) {
                try {
                    val user = userDomainService.findNotificationTargetUserById(notification.userId.value)
                    val lastActivity = user.lastActivity

                    if (lastActivity != null && lastActivity.isAfter(sentAt)) {
                        val resetNotification = notification.reset()
                        notificationDomainService.save(resetNotification)
                    }
                } catch (e: Exception) {
                    logger.warn("Failed to reset notification for user ${notification.userId.value}", e)
                }
            }
        }
    }
}
