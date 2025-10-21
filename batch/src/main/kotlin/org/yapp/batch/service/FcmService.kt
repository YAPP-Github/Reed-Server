package org.yapp.batch.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FcmService {
    private val logger = LoggerFactory.getLogger(FcmService::class.java)

    fun sendNotification(token: String, title: String, body: String): String? {
        try {
            val notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build()

            val message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build()

            val response = FirebaseMessaging.getInstance().send(message)
            logger.info("Successfully sent message: {}", response)
            return response
        } catch (e: Exception) {
            logger.error("Failed to send FCM notification", e)
            return null
        }
    }

    fun sendMulticastNotification(tokens: List<String>, title: String, body: String): List<String> {
        val successfulSends = mutableListOf<String>()

        tokens.forEach { token ->
            val messageId = sendNotification(token, title, body)
            if (messageId != null) {
                successfulSends.add(messageId)
            }
        }

        return successfulSends
    }
}
