package org.yapp.domain.user.vo

import org.yapp.domain.user.ProviderType
import org.yapp.domain.user.User

data class UserProfileVO private constructor(
    val id: User.Id,
    val email: User.Email,
    val nickname: String,
    val provider: ProviderType,
    val termsAgreed: Boolean,
    val notificationEnabled: Boolean
) {
    init {
        require(nickname.isNotBlank()) {"nickname은 비어 있을 수 없습니다."}
        require(provider.name.isNotBlank()) { "providerType은 비어 있을 수 없습니다." }
    }

    companion object {
        fun newInstance(user: User?): UserProfileVO {
            requireNotNull(user) { "User는 null일 수 없습니다." }

            return UserProfileVO(
                id = user.id,
                email = user.email,
                nickname = user.nickname,
                provider = user.providerType,
                termsAgreed = user.termsAgreed,
                notificationEnabled = user.notificationEnabled
            )
        }
    }
}
