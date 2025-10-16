package org.yapp.batch.job.notification

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.PlatformTransactionManager
import org.yapp.domain.notification.Notification
import org.yapp.domain.user.User
import org.yapp.infra.user.entity.UserEntity
import jakarta.persistence.EntityManagerFactory
import org.yapp.domain.notification.NotificationRepository
import org.yapp.domain.notification.NotificationType
import java.time.LocalDateTime

import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.JobParametersBuilder
import org.yapp.batch.service.FcmService

class NotificationJobConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val entityManagerFactory: EntityManagerFactory,
    private val notificationRepository: NotificationRepository,
    private val jobLauncher: JobLauncher,
    private val fcmService: FcmService
) {

    fun runNotificationJob() {
        val jobParameters = JobParametersBuilder()
            .addLong("time", System.currentTimeMillis())
            .toJobParameters()
        jobLauncher.run(notificationJob(), jobParameters)
    }

    fun notificationJob(): Job {
        return JobBuilder("notificationJob", jobRepository)
            .start(unrecordedNotificationStep())
            .next(dormantNotificationStep())
            .build()
    }

    fun unrecordedNotificationStep(): Step {
        return StepBuilder("unrecordedNotificationStep", jobRepository)
            .chunk<UserEntity, Notification>(100, transactionManager)
            .reader(unrecordedUserReader())
            .processor(unrecordedNotificationProcessor())
            .writer(notificationWriter())
            .build()
    }

    fun dormantNotificationStep(): Step {
        return StepBuilder("dormantNotificationStep", jobRepository)
            .chunk<UserEntity, Notification>(100, transactionManager)
            .reader(dormantUserReader())
            .processor(dormantNotificationProcessor())
            .writer(notificationWriter())
            .build()
    }

    fun unrecordedUserReader(): JpaPagingItemReader<UserEntity> {
        val queryProvider = JpaPagingItemReaderBuilder<UserEntity>()
            .name("unrecordedUserReader")
            .entityManagerFactory(entityManagerFactory)
            .pageSize(100)
            .queryString(
                "SELECT u FROM UserEntity u WHERE u.lastActivity <= :sevenDaysAgo AND u.notificationEnabled = true"
            )
            .parameterValues(mapOf("sevenDaysAgo" to LocalDateTime.now().minusDays(7)))
            .build()
        queryProvider.afterPropertiesSet()
        return queryProvider
    }

    fun dormantUserReader(): JpaPagingItemReader<UserEntity> {
        val queryProvider = JpaPagingItemReaderBuilder<UserEntity>()
            .name("dormantUserReader")
            .entityManagerFactory(entityManagerFactory)
            .pageSize(100)
            .queryString(
                "SELECT u FROM UserEntity u WHERE u.lastActivity <= :thirtyDaysAgo AND u.notificationEnabled = true"
            )
            .parameterValues(mapOf("thirtyDaysAgo" to LocalDateTime.now().minusDays(30)))
            .build()
        queryProvider.afterPropertiesSet()
        return queryProvider
    }

    fun unrecordedNotificationProcessor(): ItemProcessor<UserEntity, Notification> {
        return ItemProcessor { userEntity ->
            val user = userEntity.toDomain()
            Notification.create(
                user = user,
                title = "미기록 알림",
                message = "이번주에 읽은 책, 잊기 전에 기록해 보세요!",
                notificationType = NotificationType.UNRECORDED
            )
        }
    }

    // @Bean - Disabled to avoid conflicts with FcmNotificationJobConfig
    fun dormantNotificationProcessor(): ItemProcessor<UserEntity, Notification> {
        return ItemProcessor { userEntity ->
            val user = userEntity.toDomain()
            Notification.create(
                user = user,
                title = "휴면 알림",
                message = "그동안 읽은 책을 모아 기록해 보세요!",
                notificationType = NotificationType.DORMANT
            )
        }
    }

    // @Bean - Disabled to avoid conflicts with FcmNotificationJobConfig
    fun notificationWriter(): ItemWriter<Notification> {
        return ItemWriter { notifications ->
            notifications.forEach { notification ->
                // Save notification to database
                notificationRepository.save(notification)

                // Send FCM notification if token is available
                if (notification.fcmToken.isNotBlank()) {
                    fcmService.sendNotification(
                        token = notification.fcmToken,
                        title = notification.title,
                        body = notification.message
                    )
                }
            }
        }
    }
}
