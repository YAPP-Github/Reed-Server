package org.yapp.apis.auth.dto.response

import org.yapp.domain.auth.ProviderType
import java.util.*

data class UserProfileResponse(
    val id: UUID,
    val email: String,
    val nickname: String,
    val provider: ProviderType
) {
    companion object {
        fun of(
            id: UUID,
            email: String,
            nickname: String,
            provider: ProviderType
        ): UserProfileResponse {
            return UserProfileResponse(
                id = id,
                email = email,
                nickname = nickname,
                provider = provider
            )
        }
    }
}
