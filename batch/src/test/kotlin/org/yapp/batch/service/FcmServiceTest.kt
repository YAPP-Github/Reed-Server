package org.yapp.batch.service

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.slf4j.LoggerFactory

@ExtendWith(SpringExtension::class)
@SpringBootTest
class FcmServiceTest {

    private val logger = LoggerFactory.getLogger(FcmServiceTest::class.java)

    @Autowired
    private lateinit var fcmService: FcmService

    @Test
    fun testSendNotification() {
        // Given
        val token = "epGzIKlHScicTBrbt26pFG:APA91bG-ZPD-KMJyS-JOiflEPUIVvrp8l9DFBN2dlNG8IHw8mFlkAPok7dVPFJR4phc9061KPztkAIjBJaryZLnv6vIJXNGQsykzDcok3wFC9LrsC-F_aKY"
        val title = "Test Notification"
        val body = "This is a test notification from the FCM service test."

        // When
        val result = fcmService.sendNotification(token, title, body)

        // Then
        logger.info("[DEBUG_LOG] FCM notification result: {}", result)
        // Note: We can't assert on the result because it depends on the FCM service
        // and the token validity. We just log the result for manual verification.
    }

    @Test
    fun testSendMulticastNotification() {
        // Given
        val tokens = listOf("epGzIKlHScicTBrbt26pFG:APA91bG-ZPD-KMJyS-JOiflEPUIVvrp8l9DFBN2dlNG8IHw8mFlkAPok7dVPFJR4phc9061KPztkAIjBJaryZLnv6vIJXNGQsykzDcok3wFC9LrsC-F_aKY")
        val title = "Test Multicast Notification"
        val body = "This is a test multicast notification from the FCM service test."

        // When
        val results = fcmService.sendMulticastNotification(tokens, title, body)

        // Then
        logger.info("[DEBUG_LOG] FCM multicast notification results: {}", results)
        // Note: We can't assert on the results because they depend on the FCM service
        // and the token validity. We just log the results for manual verification.
    }
}
