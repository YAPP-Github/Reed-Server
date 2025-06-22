package org.yapp.apis.auth.dto.response

import java.util.*

data class UserProfileResponse(
    val id: UUID,
    val email: String,
    val nickname: String,
    val provider: String
)
