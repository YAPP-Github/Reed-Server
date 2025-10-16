package org.yapp.apis.notification.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class FCMMessageFormatDto(
    @JsonProperty("message")
    val message: Message,
    @JsonProperty("validate_only")
    val validateOnly: Boolean
) {
    data class Message(
        @JsonProperty("token")
        val token: String,
        @JsonProperty("notification")
        val notification: Notification,
        @JsonProperty("data")
        val data: Data
    )

    data class Notification(
        @JsonProperty("title")
        val title: String,
        @JsonProperty("body")
        val body: String
    )

    data class Data(
        @JsonProperty("name")
        val name: String,
        @JsonProperty("description")
        val description: String
    )
}
