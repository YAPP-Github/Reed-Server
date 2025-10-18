package org.yapp.batch.job.fcm

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.yapp.batch.service.FcmService
import org.yapp.domain.notification.Notification
import org.yapp.domain.notification.NotificationRepository
import org.yapp.domain.notification.NotificationType
import org.yapp.domain.user.ProviderType
import org.yapp.domain.user.User
import org.yapp.domain.user.UserRepository
import org.yapp.globalutils.auth.Role
import org.yapp.globalutils.util.UuidGenerator
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class FcmNotificationJobConfigTest {

    @InjectMocks
    private lateinit var fcmNotificationJobConfig: FcmNotificationJobConfig

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var notificationRepository: NotificationRepository

    @Mock
    private lateinit var fcmService: FcmService

    @Captor
    private lateinit var notificationCaptor: ArgumentCaptor<Notification>

    private val testFcmToken = "epGzIKlHScicTBrbt26pFG:APA91bG-ZPD-KMJyS-JOiflEPUIVvrp8l9DFBN2dlNG8IHw8mFlkAPok7dVPFJR4phc9061KPztkAIjBJaryZLnv6vIJXNGQsykzDcok3wFC9LrsC-F_aKY"
    private lateinit var testUser: User
    private lateinit var testNotification: Notification

    @BeforeEach
    fun setUp() {
        // Create a test user
        testUser = User.reconstruct(
            id = User.Id.newInstance(UuidGenerator.create()),
            email = User.Email.newInstance("test@example.com"),
            nickname = "Test User",
            profileImageUrl = null,
            providerType = ProviderType.KAKAO,
            providerId = User.ProviderId.newInstance("12345"),
            role = Role.USER,
            termsAgreed = true,
            appleRefreshToken = null,
            fcmToken = testFcmToken,
            notificationEnabled = true,
            lastActivity = LocalDateTime.now().minusDays(10),
            createdAt = LocalDateTime.now().minusDays(30),
            updatedAt = LocalDateTime.now().minusDays(10),
            deletedAt = null
        )

        // Create a test notification
        testNotification = Notification.create(
            user = testUser,
            title = "Test Notification",
            message = "This is a test notification",
            notificationType = NotificationType.UNRECORDED
        )

        // Reset mocks
        reset(userRepository, notificationRepository, fcmService)
    }

    @Test
    fun `test checkAndSendNotifications sends notifications to inactive users`() {
        // Given
        val inactiveUsers = listOf(testUser)
        `when`(userRepository.findByLastActivityBeforeAndNotificationEnabled(any(), eq(true)))
            .thenReturn(inactiveUsers)

        `when`(notificationRepository.findByUserId(testUser.id.value))
            .thenReturn(emptyList())

        `when`(notificationRepository.save(any()))
            .thenReturn(testNotification)

        `when`(fcmService.sendMulticastNotification(anyList(), anyString(), anyString()))
            .thenReturn(listOf("message-id-123"))

        // When
        fcmNotificationJobConfig.checkAndSendNotifications()

        // Then
        verify(userRepository, times(2)).findByLastActivityBeforeAndNotificationEnabled(any(), eq(true))
        verify(notificationRepository, atLeastOnce()).findByUserId(testUser.id.value)
        verify(notificationRepository, atLeastOnce()).save(any())
        verify(fcmService, atLeastOnce()).sendMulticastNotification(anyList(), anyString(), anyString())

        // Log for debugging
        println("[DEBUG_LOG] Test completed successfully")
    }

    @Test
    fun `test checkAndSendNotifications does not send duplicate notifications`() {
        // Given
        val inactiveUsers = listOf(testUser)
        val existingNotification = Notification.reconstruct(
            id = Notification.Id.newInstance(UuidGenerator.create()),
            user = testUser,
            fcmToken = testFcmToken,
            title = "Previous Notification",
            message = "This is a previous notification",
            notificationType = NotificationType.UNRECORDED,
            isRead = false,
            isSent = true,
            sentAt = LocalDateTime.now().minusDays(1),
            createdAt = LocalDateTime.now().minusDays(1),
            updatedAt = LocalDateTime.now().minusDays(1)
        )

        `when`(userRepository.findByLastActivityBeforeAndNotificationEnabled(any(), eq(true)))
            .thenReturn(inactiveUsers)

        `when`(notificationRepository.findByUserId(testUser.id.value))
            .thenReturn(listOf(existingNotification))

        // When
        fcmNotificationJobConfig.checkAndSendNotifications()

        // Then
        verify(userRepository, times(2)).findByLastActivityBeforeAndNotificationEnabled(any(), eq(true))
        verify(notificationRepository, atLeastOnce()).findByUserId(testUser.id.value)
        verify(fcmService, never()).sendMulticastNotification(anyList(), anyString(), anyString())

        // Log for debugging
        println("[DEBUG_LOG] Test completed successfully")
    }

    @Test
    fun `test checkAndSendNotifications resets notifications for active users`() {
        // Given
        // Create a new active user instead of using copy
        val activeUser = User.reconstruct(
            id = testUser.id,
            email = testUser.email,
            nickname = testUser.nickname,
            profileImageUrl = testUser.profileImageUrl,
            providerType = testUser.providerType,
            providerId = testUser.providerId,
            role = testUser.role,
            termsAgreed = testUser.termsAgreed,
            appleRefreshToken = testUser.appleRefreshToken,
            fcmToken = testUser.fcmToken,
            notificationEnabled = testUser.notificationEnabled,
            lastActivity = LocalDateTime.now(), // Set to now to make it active
            createdAt = testUser.createdAt,
            updatedAt = testUser.updatedAt,
            deletedAt = testUser.deletedAt
        )

        val sentNotification = Notification.reconstruct(
            id = Notification.Id.newInstance(UuidGenerator.create()),
            user = activeUser,
            fcmToken = testFcmToken,
            title = "Previous Notification",
            message = "This is a previous notification",
            notificationType = NotificationType.UNRECORDED,
            isRead = false,
            isSent = true,
            sentAt = LocalDateTime.now().minusDays(1),
            createdAt = LocalDateTime.now().minusDays(1),
            updatedAt = LocalDateTime.now().minusDays(1)
        )

        `when`(userRepository.findByLastActivityBeforeAndNotificationEnabled(any(), eq(true)))
            .thenReturn(emptyList())

        `when`(notificationRepository.findBySent(true))
            .thenReturn(listOf(sentNotification))

        `when`(notificationRepository.save(any()))
            .thenReturn(sentNotification)

        // When
        fcmNotificationJobConfig.checkAndSendNotifications()

        // Then
        verify(userRepository, times(2)).findByLastActivityBeforeAndNotificationEnabled(any(), eq(true))
        verify(notificationRepository).findBySent(true)
        verify(notificationRepository).save(any())

        // Log for debugging
        println("[DEBUG_LOG] Test completed successfully")
    }
}
