package org.yapp.batch.job.fcm

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.yapp.batch.service.NotificationService

@Configuration
@EnableScheduling
class FcmNotificationJobConfig(
    private val notificationService: NotificationService
) {
    private val logger = LoggerFactory.getLogger(FcmNotificationJobConfig::class.java)

    companion object {
        private const val UNRECORDED_DAYS_THRESHOLD = 7
        private const val DORMANT_DAYS_THRESHOLD = 30
    }

    @Scheduled(fixedRate = 60000)
    fun checkAndSendNotifications() {
        logger.info("========== Starting FCM notification job ==========")

        val (unrecordedUserCount, unrecordedDeviceCount) = notificationService.sendUnrecordedNotifications(UNRECORDED_DAYS_THRESHOLD)
        val (dormantUserCount, dormantDeviceCount) = notificationService.sendDormantNotifications(DORMANT_DAYS_THRESHOLD)
        notificationService.resetNotificationsForActiveUsers()

        logger.info(
            "========== Completed FCM notification job ========== \n" +
            "Summary:\n" +
            "  - Unrecorded: $unrecordedUserCount users, $unrecordedDeviceCount devices\n" +
            "  - Dormant: $dormantUserCount users, $dormantDeviceCount devices"
        )
    }
}
