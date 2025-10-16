package org.yapp.apis.notification.dto

data class FCMMessageRequestDto(
    val userId: String,
    val title: String,
    val body: String,
    val isEnd: String
)