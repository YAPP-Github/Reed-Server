package org.yapp.apis.notification.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.auth.oauth2.GoogleCredentials
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.yapp.apis.notification.dto.FCMMessageFormatDto
import org.yapp.apis.notification.dto.FCMMessageRequestDto
import java.io.IOException

@Service
class FcmService(
    private val objectMapper: ObjectMapper
) {
    @Value("\${fcm.key.path}")
    private lateinit var firebaseJson: String

    @Value("\${fcm.key.scope}")
    private lateinit var fcmUrl: String

    fun sendMessageTo(fcmToken: String, fcmMessageRequestDto: FCMMessageRequestDto) {
        val title = fcmMessageRequestDto.title
        val body = fcmMessageRequestDto.body
        val id = fcmMessageRequestDto.userId
        val isEnd = fcmMessageRequestDto.isEnd
        val message = makeMessage(fcmToken, title, body, id, isEnd)

        val client = OkHttpClient()
        val requestBody = message.toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url(fcmUrl)
            .post(requestBody)
            .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
            .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            // log response.body.string()
        }
    }

    private fun makeMessage(targetToken: String, title: String, body: String, name: String, description: String): String {
        val fcmMessage = FCMMessageFormatDto(
            message = FCMMessageFormatDto.Message(
                token = targetToken,
                notification = FCMMessageFormatDto.Notification(
                    title = title,
                    body = body
                ),
                data = FCMMessageFormatDto.Data(
                    name = name,
                    description = description
                )
            ),
            validateOnly = false
        )

        return objectMapper.writeValueAsString(fcmMessage)
    }

    private fun getAccessToken(): String {
        val googleCredentials = GoogleCredentials
            .fromStream(ClassPathResource(firebaseJson).inputStream)
            .createScoped(listOf("https://www.googleapis.com/auth/cloud-platform"))

        googleCredentials.refreshIfExpired()

        return googleCredentials.accessToken.tokenValue
    }
}
