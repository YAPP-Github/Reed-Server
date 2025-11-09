package org.yapp.batch.service

import com.google.firebase.messaging.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.yapp.batch.dto.FcmSendResult

@Service
class FcmService {
    private val logger = LoggerFactory.getLogger(FcmService::class.java)

    fun sendMulticastNotification(tokens: List<String>, title: String, body: String): FcmSendResult {
        if (tokens.isEmpty()) {
            logger.warn("FCM token list is empty. Skipping notification.")
            return FcmSendResult.empty()
        }

        val notification = buildNotification(title, body)
        val messages = tokens.map { token ->
            Message.builder()
                .setNotification(notification)
                .setToken(token)
                .build()
        }

        try {
            val response = FirebaseMessaging.getInstance().sendEach(messages)
            return processFcmResponse(response, tokens)
        } catch (e: FirebaseMessagingException) {
            logger.error("Failed to send FCM notification to ${tokens.size} tokens", e)
            return FcmSendResult.allFailed(tokens.size)
        }
    }

    private fun buildNotification(title: String, body: String): Notification {
        return Notification.builder()
            .setTitle(title)
            .setBody(body)
            .build()
    }

    private fun processFcmResponse(response: BatchResponse, tokens: List<String>): FcmSendResult {
        val invalidTokens = mutableListOf<String>()
        val noFailures = 0

        if (response.failureCount > noFailures) {
            response.responses.forEachIndexed { index, sendResponse ->
                if (!sendResponse.isSuccessful) {
                    val failedToken = tokens[index]
                    val errorCode = sendResponse.exception?.messagingErrorCode
                    if (errorCode == MessagingErrorCode.UNREGISTERED || errorCode == MessagingErrorCode.INVALID_ARGUMENT) {
                        invalidTokens.add(failedToken)
                        logger.warn("Invalid FCM token found: {}. Error: {}", failedToken, errorCode)
                    } else {
                        logger.error("Failed to send to token: {}. Error: {}", failedToken, errorCode, sendResponse.exception)
                    }
                }
            }
        }

        logger.info(
            "FCM multicast message sent. Success: {}, Failure: {}, Invalid Tokens: {}",
            response.successCount,
            response.failureCount,
            invalidTokens.size
        )

        return FcmSendResult.of(
            successCount = response.successCount,
            failureCount = response.failureCount,
            invalidTokens = invalidTokens
        )
    }
}
