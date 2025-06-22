package org.yapp.apis.auth.dto.response

data class UserProfileResponse(
    val id: Long,
    val email: String,
    val nickname: String,
    val provider: String
)